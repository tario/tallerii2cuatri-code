package mereditor.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mereditor.modelo.base.Componente;
import mereditor.modelo.base.ComponenteNombre;
import mereditor.modelo.base.ComponenteAtributos;
import mereditor.modelo.validacion.Observacion;

public class Entidad extends ComponenteNombre implements ComponenteAtributos {

	public enum TipoEntidad {
		MAESTRA_COSA, MAESTRA_DOMINIO, TRANSACCIONAL_HISTORICA, TRANSACCIONAL_PROGRAMADA
	}

	protected TipoEntidad tipo = TipoEntidad.MAESTRA_COSA;
	protected Set<Atributo> atributos = new HashSet<>();
	protected Set<Identificador> identificadores = new HashSet<>();

	/**
	 * Relaciones a las que pertence la entidad.
	 */
	protected Set<Relacion> relaciones = new HashSet<>();

	public Entidad() {
		super();
	}

	public Entidad(String nombre) {
		super(nombre);
	}

	public Entidad(String nombre, String id, TipoEntidad tipo) {
		super(nombre, id);
		this.tipo = tipo;
	}

	public Entidad(String nombre, String id, Componente padre, TipoEntidad tipo) {
		super(nombre, id, padre);
		this.tipo = tipo;
	}

	public void addIdentificador(Identificador identificador) {
		identificadores.add(identificador);
	}

	public void removeIdentificador(Identificador identificador) {
		identificadores.remove(identificador);
	}

	public void addAtributo(Atributo atributo) {
		this.atributos.add(atributo);
		atributo.setPadre(this);
	}

	public void removeAtributo(Atributo atributo) {
		this.atributos.remove(atributo);
		// Lista donde se guardan los identificadores a editar
		// se debe hacer de esta forma dado que no se puede modificar
		// una collection que se esta recorriendo con un iterador.
		List<Identificador> identificadores = new ArrayList<>();

		// Recorrer los identificadores para buscar el atributo y sacarlo.
		for (Identificador identificador : this.identificadores)
			if (identificador.contiene(atributo))
				identificadores.add(identificador);

		for (Identificador identificador : this.identificadores)
			identificador.removeAtributo(atributo);
	}

	public void addRelacion(Relacion relacion) {
		this.relaciones.add(relacion);
	}

	public void removeRelacion(Relacion relacion) {
		this.relaciones.remove(relacion);
	}

	public Collection<Atributo> getAtributos() {
		return Collections.unmodifiableCollection(this.atributos);
	}

	public Set<Identificador> getIdentificadores() {
		return Collections.unmodifiableSet(this.identificadores);
	}

	public Set<Relacion> getRelaciones() {
		return Collections.unmodifiableSet(this.relaciones);
	}
	
	@Override
	public Collection<Componente> getComponentes() {
		return new ArrayList<Componente>(this.atributos);
	}

	/**
	 * Devuelve la relacion que conecta a las dos entidades.
	 * 
	 * @param entidad
	 * @return
	 */
	public Relacion relacion(Entidad entidad) {
		Set<Relacion> interseccion = new HashSet<>(this.relaciones);
		interseccion.retainAll(entidad.relaciones);

		if (interseccion.size() > 0)
			return interseccion.iterator().next();

		return null;
	}

	public TipoEntidad getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoEntidad tipo) {
		this.tipo = tipo;
	}
	public boolean esGenerica(){
		for(Componente diagramaPadre: this.getAllPadres()){
			for(Jerarquia jerarquia: ((Diagrama)diagramaPadre).getJerarquias(true)){
				if(jerarquia.getGenerica().equals(this))
					return true;
			}
		}
		return false;
	}
	public boolean esDerivada(){
		for(Componente diagramaPadre: this.getAllPadres()){
			for(Jerarquia jerarquia: ((Diagrama)diagramaPadre).getJerarquias(true)){
				for(Entidad derivada: jerarquia.getDerivadas()){
					if(derivada.equals(this))
						return true;
				}
			}
		}
		return false;
	}
	public Set<Entidad> getDerivadas(){

		for(Componente diagramaPadre: this.getAllPadres()){
			for(Jerarquia jerarquia: ((Diagrama)diagramaPadre).getJerarquias(true)){
				if(jerarquia.getGenerica().equals(this))
					return jerarquia.getDerivadas();
			}
		}
		return new HashSet<Entidad>();
	}
	public Entidad getGenerica(){

		for(Componente diagramaPadre: this.getAllPadres()){
			for(Jerarquia jerarquia: ((Diagrama)diagramaPadre).getJerarquias(true)){
				for(Entidad derivada: jerarquia.getDerivadas()){
					if(derivada.equals(this))
						return jerarquia.getGenerica();
					
				}
			}
		}
		return null;
	}
	public boolean esJerarquica(){
		return esGenerica()||esDerivada();
	}
	public int getNivel(){
		int nivel=0;
		if(!esGenerica())
			return nivel;
		for(Entidad derivada: getDerivadas()){
			int nivelDerivada=derivada.getNivel();
			if( nivel < nivelDerivada+1)
				nivel=nivelDerivada+1;
		}
		return nivel;
	}

	@Override
	public Observacion validar() {
		Observacion observacion = super.validar();

		for (Atributo atributo : this.atributos)
			observacion.addObservacion(atributo.validar());

		return observacion;
	}

	@Override
	public boolean contiene(Componente componente) {
		boolean contiene = this.atributos.contains(componente);
		if (contiene)
			return contiene;
		for (Componente hijo : this.atributos) {
			contiene = hijo.contiene(componente);
			if (contiene)
				return contiene;
		}
		return false;
	}

	/**
	 * Representa un identificador de una identidad. Contiene referencias a los
	 * Atributos y Entidades que componen el Identificador.
	 */
	public class Identificador {
		private Entidad entidad;
		private Set<Atributo> atributos = new HashSet<>();
		private Set<Entidad> entidades = new HashSet<>();

		public Identificador(Entidad entidad) {
			if (entidad == null)
				throw new IllegalArgumentException("Entidad no puede ser null.");

			this.entidad = entidad;
		}

		public void addAtributo(Atributo atributo) {
			if (!this.entidad.contiene(atributo))
				throw new IllegalArgumentException(
						"Un atributo debe pertenecer a la entidad para ser identificador.");

			this.atributos.add(atributo);
		}

		public void removeAtributo(Atributo atributo) {
			this.atributos.remove(atributo);
			this.verificarVacio();
		}

		public void addEntidad(Entidad entidad) {
			if (this.entidad.equals(entidad))
				throw new RuntimeException(
						"Una Entidad no puede ser su propio identificador.");

			this.entidades.add(entidad);
		}

		public void removeEntidad(Entidad entidad) {
			this.entidades.remove(entidad);
			this.verificarVacio();
		}

		/**
		 * Verifica si el identificador no tiene ningun atributo o entidad y lo
		 * elimina en ese caso.
		 */
		private void verificarVacio() {
			if (this.atributos.isEmpty() && this.entidades.isEmpty())
				this.entidad.identificadores.remove(this);
		}

		public Set<Atributo> getAtributos() {
			return Collections.unmodifiableSet(atributos);
		}

		public Set<Entidad> getEntidades() {
			return Collections.unmodifiableSet(entidades);
		}

		/*
		 * Devuelve la entidad de la cual es identificador.
		 */
		public Entidad getEntidad() {
			return this.entidad;
		}

		public boolean isInterno() {
			return this.entidades.isEmpty();
		}

		public boolean isExterno() {
			return this.atributos.isEmpty();
		}

		public boolean isMixto() {
			return !this.atributos.isEmpty() && !this.entidades.isEmpty();
		}

		/**
		 * Indica si el identificador contiene al componente especificado.
		 * 
		 * @param componente
		 * @return
		 */
		public boolean contiene(Componente componente) {
			return this.atributos.contains(componente)
					|| this.entidades.contains(componente);
		}
	}
}
