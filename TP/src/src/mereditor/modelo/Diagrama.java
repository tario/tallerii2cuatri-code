package mereditor.modelo;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mereditor.modelo.Validacion.EstadoValidacion;
import mereditor.modelo.base.Componente;
import mereditor.modelo.base.ComponenteAtributos;
import mereditor.modelo.base.ComponenteNombre;
import mereditor.modelo.validacion.Observacion;
import mereditor.modelo.validacion.ValidarAcoplamiento;
import mereditor.modelo.validacion.ValidarClaridadAtributos;
import mereditor.modelo.validacion.ValidarClaridadComponentes;
import mereditor.modelo.validacion.ValidarSuperposicion;

public class Diagrama extends ComponenteNombre {

	protected Set<Diagrama> diagramas = new HashSet<Diagrama>();
	protected Set<Componente> componentes = new HashSet<Componente>();
	protected Validacion validacion = new Validacion();
	protected Proyecto proyecto;

	public Diagrama(Proyecto proyecto) {
		super();
		this.proyecto = proyecto;
	}

	/**
	 * Devuelve los diagramas hijos de este diagrama.
	 * @return
	 */
	public Set<Diagrama> getDiagramas() {
		return Collections.unmodifiableSet(diagramas);
	}

	@Override
	public Set<Componente> getComponentes() {
		return Collections.unmodifiableSet(componentes);
	}

	/**
	 * Obtiene las entidades de este diagrama y sus ancestros.
	 * 
	 * @param incluirAncestros
	 *            Indica si se deben incluir las entidades de los ancestros.
	 * @return
	 */
	public Set<Entidad> getEntidades(boolean incluirAncestros) {
		Set<Entidad> entidades = Componente.filtrarComponentes(Entidad.class, this.componentes);

		if (incluirAncestros && this.getPadre() != null) {
			Diagrama diagrama = (Diagrama) this.getPadre();
			entidades.addAll(diagrama.getEntidades(incluirAncestros));
		}

		return entidades;
	}

	/**
	 * Obtiene una colección con todos los atributos de los componentes de este
	 * diagrama.
	 * 
	 * @param incluirAncestros
	 * @return
	 */
	public Collection<Atributo> getAtributos(boolean incluirAncestros) {
		Set<ComponenteAtributos> componentes = Componente.filtrarComponentes(
				ComponenteAtributos.class, this.componentes);

		Set<Atributo> atributos = new HashSet<>();

		for (ComponenteAtributos componente : componentes)
			atributos.addAll(componente.getAtributos());

		if (incluirAncestros && this.getPadre() != null) {
			Diagrama diagrama = (Diagrama) this.getPadre();
			atributos.addAll(diagrama.getAtributos(incluirAncestros));
		}

		return atributos;
	}

	/**
	 * Obtiene una colección con todas las Relaciones de este diagrama.
	 * 
	 * @param incluirAncestros
	 * @return
	 */
	public Set<Relacion> getRelaciones(boolean incluirAncestros) {
		Set<Relacion> relaciones = Componente.filtrarComponentes(Relacion.class, this.componentes);

		if (incluirAncestros && this.getPadre() != null) {
			Diagrama diagrama = (Diagrama) this.getPadre();
			relaciones.addAll(diagrama.getRelaciones(incluirAncestros));
		}

		return relaciones;
	}

	/**
	 * Obtiene una colección con todas las Jerarquias de este diagrama.
	 * 
	 * @param incluirAncestros
	 * @return
	 */
	public Set<Jerarquia> getJerarquias(boolean incluirAncestros) {
		Set<Jerarquia> jerarquias = Componente
				.filtrarComponentes(Jerarquia.class, this.componentes);

		if (incluirAncestros && this.getPadre() != null) {
			Diagrama diagrama = (Diagrama) this.getPadre();
			jerarquias.addAll(diagrama.getJerarquias(incluirAncestros));
		}

		return jerarquias;
	}

	/**
	 * Devuelve la información de validacion del diagrama.
	 * 
	 * @return
	 */
	public Validacion getValidacion() {
		return this.validacion;
	}

	@Override
	public void addValidaciones() {
		this.validaciones.add(new ValidarAcoplamiento());
		this.validaciones.add(new ValidarClaridadComponentes());
		this.validaciones.add(new ValidarClaridadAtributos());
		this.validaciones.add(new ValidarSuperposicion());
	}

	@Override
	public Observacion validar() {
		Observacion observacion = super.validar();

		for (Componente componente : this.componentes)
			observacion.addObservacion(componente.validar());

		if (!observacion.isEmpty()) {
			this.validacion.setObservaciones(observacion.toString());
			this.validacion.setEstado(EstadoValidacion.VALIDADO_CON_OBSERVACIONES);
		} else
			this.validacion.setEstado(EstadoValidacion.VALIDADO);

		return observacion;
	}

	/**
	 * Agrega el componente a este diagrama. Si es un Diagrama lo agrega a
	 * conjunto de diagramas de lo contrario al conjunto de componentes.
	 * 
	 * @param componente
	 */
	public void agregar(Componente componente) {
		componente.setPadre(this);
		if (Diagrama.class.isInstance(componente))
			this.diagramas.add((Diagrama) componente);
		else
			this.componentes.add(componente);
	}

	@Override
	public boolean contiene(Componente componente) {
		boolean contiene = this.diagramas.contains(componente)
				|| this.componentes.contains(componente);
		if (contiene)
			return contiene;
		for (Componente hijo : this.componentes) {
			contiene = hijo.contiene(componente);
			if (contiene)
				return contiene;
		}
		return false;
	}

	@Override
	public void removePadre(String id) {
		super.removePadre(id);

		// Si no tiene padre borrar la relacion con todos los hijos de este
		// diagrama.
		if (this.padres.isEmpty()) {
			for (Diagrama diagrama : new HashSet<>(this.getDiagramas()))
				this.eliminar(diagrama);
			
			for (Componente componente : new HashSet<>(this.getComponentes()))
				this.eliminar(componente);
		}
	}

	/**
	 * Quita el componente hijo de este diagrama. Si este es el único padre del
	 * componente, lo quita del proyecto también.
	 * 
	 * @param componente
	 */
	public void eliminar(Componente componente) {
		if (Diagrama.class.isInstance(componente))
			this.diagramas.remove((Diagrama) componente);
		else
			this.componentes.remove(componente);

		componente.removePadre(this.id);

		this.proyecto.eliminar(componente);
	}
}
