package mereditor.modelo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mereditor.modelo.base.Componente;
import mereditor.modelo.base.ComponenteNombre;

public class Diagrama extends ComponenteNombre {

	protected Set<Diagrama> diagramas = new HashSet<Diagrama>();
	protected Set<Componente> componentes = new HashSet<Componente>();
	protected Proyecto proyecto;

	public Set<Diagrama> getDiagramas() {
		return Collections.unmodifiableSet(diagramas);
	}

	@Override
	public Set<Componente> getComponentes() {
		return Collections.unmodifiableSet(componentes);
	}

	/**
	 * Agrega el componente a este diagrama. Si es un DiagramaDER lo agrega a
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
		boolean contiene = this.diagramas.contains(componente) || this.componentes.contains(componente);
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
		if (DiagramaDER.class.isInstance(componente))
			this.diagramas.remove((DiagramaDER) componente);
		else
			this.componentes.remove(componente);

		componente.removePadre(this.id);

		this.proyecto.eliminar(componente);
	}

}
