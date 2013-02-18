package mereditor.modelo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mereditor.modelo.base.Componente;
import mereditor.modelo.validacion.Observacion;

public class Jerarquia extends Componente {
	public enum TipoJerarquia {
		TOTAL_EXCLUSIVA, TOTAL_SUPERPUESTA, PARCIAL_EXCLUSIVA, PARCIAL_SUPERPUESTA
	}

	protected Entidad generica;
	protected Set<Entidad> derivadas = new HashSet<>();

	protected TipoJerarquia tipo = TipoJerarquia.TOTAL_EXCLUSIVA;

	public Jerarquia() {
		super();
	}

	public Jerarquia(String id) {
		super(id);
	}

	public Set<Entidad> getDerivadas() {
		return Collections.unmodifiableSet(derivadas);
	}

	public void addDerivada(Entidad entidad) {
		this.derivadas.add(entidad);
	}

	public void removeDerivada(Entidad entidad) {
		this.derivadas.remove(entidad);
	}

	public void setGenerica(Entidad generica) {
		this.generica = generica;
	}

	public Entidad getGenerica() {
		return this.generica;
	}

	public TipoJerarquia getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoJerarquia tipo) {
		this.tipo = tipo;
	}
	
	public Entidad getRaiz(){
		Entidad raiz=this.getGenerica();
		while(raiz.esDerivada()){
			raiz=raiz.getGenerica();
		}
		return raiz;
	}
	
	@Override
	public Observacion validar() {
		Observacion observacion = super.validar();

		if (this.derivadas.isEmpty())
			observacion.addObservacion(new Observacion(this, "No tiene entidades derivadas establecidas."));
		
		if (this.generica == null)
			observacion.addObservacion(new Observacion(this, "No tiene entidad genérica establecida."));

		return observacion;
	}

	@Override
	public String toString() {
		return "Jerarquia: " + this.generica.getNombre();
	}
}
