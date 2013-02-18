package mereditor.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mereditor.modelo.Atributo.TipoAtributo;
import mereditor.modelo.Entidad.Identificador;
import mereditor.modelo.base.Componente;
import mereditor.modelo.base.ComponenteAtributos;
import mereditor.modelo.base.ComponenteCardinal;
import mereditor.modelo.base.ComponenteNombre;
import mereditor.modelo.base.ComponenteTipado;
import mereditor.modelo.validacion.Observacion;
import mereditor.modelo.validacion.ValidarAtributoTipo;
import mereditor.modelo.validacion.ValidarCardinalidadCompleta;
import mereditor.modelo.validacion.ValidarTipoCompleto;

public class Atributo extends ComponenteNombre implements ComponenteAtributos,
		ComponenteTipado<TipoAtributo>, ComponenteCardinal {
	public enum TipoAtributo {
		CARACTERIZACION, DERIVADO_COPIA, DERIVADO_CALCULO
	}

	protected TipoAtributo tipo;
	protected String cardinalidadMinima = "1";
	protected String cardinalidadMaxima = "1";
	protected Set<Atributo> atributos = new HashSet<Atributo>();

	// Derivado copia
	protected Atributo original;

	// Derivado calculo
	protected String formula;

	public Atributo() {
		super();
	}

	public Atributo(String nombre) {
		super(nombre);
	}

	public Atributo(String nombre, String id, Componente padre) {
		super(nombre, id, padre);
	}

	public Atributo(String nombre, String id, Componente padre, String min, String cardMax,
			TipoAtributo tipo, Set<Atributo> atributos) {
		this(nombre, id, padre);
		this.atributos = atributos;
		this.cardinalidadMinima = min;
		this.cardinalidadMaxima = cardMax;
	}

	public TipoAtributo getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoAtributo tipo) {
		this.tipo = tipo;
	}

	public String getCardinalidadMaxima() {
		return this.cardinalidadMaxima;
	}

	public void setCardinalidadMaxima(String cardinalidad) {
		if(cardinalidad.isEmpty())
			return;
		this.cardinalidadMaxima = cardinalidad;
	}

	public String getCardinalidadMinima() {
		return this.cardinalidadMinima;
	}

	public void setCardinalidadMinima(String cardinalidad) {
		this.cardinalidadMinima = cardinalidad;
	}

	public Atributo getOriginal() {
		return this.original;
	}

	public void setOriginal(Atributo original) {
		this.original = original;
	}

	public String getFormula() {
		return this.formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public void addAtributo(Atributo atributo) {
		this.atributos.add(atributo);
		atributo.setPadre(this);
	}

	public void removeAtributo(Atributo atributo) {
		this.atributos.remove(atributo);
	}

	public Collection<Atributo> getAtributos() {
		return Collections.unmodifiableCollection(this.atributos);
	}
	
	@Override
	public Collection<Componente> getComponentes() {
		return new ArrayList<Componente>(this.atributos);
	}

	/**
	 * Indica si tiene atributos hijos.
	 * 
	 * @return
	 */
	public boolean esCompuesto() {
		return !this.atributos.isEmpty();
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
	 * Indica si el atributo es un identificador principal.
	 * 
	 * @return
	 */
	public boolean esIdentificador() {
		if (Entidad.class.isInstance(this.getPadre())) {
			Set<Identificador> identificadores = ((Entidad) this.getPadre()).identificadores;

			for (Identificador identificador : identificadores) {
				if (identificador.contiene(this) && identificador.getEntidades().isEmpty()
						&& identificador.getAtributos().size() == 1)
					return true;
			}
		}

		return false;
	}

	@Override
	public void addValidaciones() {
		this.validaciones.add(new ValidarTipoCompleto());
		this.validaciones.add(new ValidarAtributoTipo());
		this.validaciones.add(new ValidarCardinalidadCompleta());
		super.addValidaciones();
	}

	@Override
	public Observacion validar() {
		Observacion observacion = super.validar();

		for (Atributo atributo : this.atributos)
			observacion.addObservacion(atributo.validar());

		return observacion;
	}
}
