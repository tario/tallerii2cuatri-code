package mereditor.modelo.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import mereditor.modelo.validacion.Observacion;
import mereditor.modelo.validacion.Validable;

public abstract class Componente implements Comparable<Componente>, Validable {
	/**
	 * Id del componente
	 */
	protected String id;

	/**
	 * Id del padre del componente
	 */
	protected Map<String, Componente> padres = new HashMap<>();

	/**
	 * Lista de validaciones.
	 */
	protected List<mereditor.modelo.validacion.Validacion> validaciones = new ArrayList<>();

	public Componente(String id) {
		this.id = id;
		this.addValidaciones();
	}

	public Componente() {
		this(UUID.randomUUID().toString());
	}

	public Componente(String id, Componente padre) {
		this(id);
		this.setPadre(padre);
	}

	public String getId() {
		return id;
	}

	/**
	 * Obtiene el padre de este componente si tiene uno sólo. En el caso de
	 * tener más de un padre genera una excepción. Este método debe usar sólo
	 * cuando se sabe que el componente tendrá un sólo padre.
	 * 
	 * @return El componente padre si tiene uno o null si no tiene ningún padre.
	 */
	public Componente getPadre() {
		if (this.padres.size() > 1)
			throw new RuntimeException("Tiene más de un padre.");

		return this.padres.isEmpty() ? null : padres.values().iterator().next();
	}

	/**
	 * Devuelve el padre que tiene el id especificado o null si no es padre de
	 * este componente.
	 * 
	 * @param id
	 * @return
	 */
	public Componente getPadre(String id) {
		return padres.get(id);
	}

	/**
	 * Obtener una coleccion de todos los padres de este componente.
	 * 
	 * @return Colleccion de componentes padres.
	 */
	public Collection<Componente> getAllPadres() {
		return Collections.unmodifiableCollection(this.padres.values());
	}

	/**
	 * Especificar un padre para este componente.
	 * 
	 * @param padre
	 */
	public void setPadre(Componente padre) {
		if ( padre != null)
			this.padres.put(padre.getId(), padre);
	}

	/**
	 * Eliminar el componente del id especificado como padre de este componente.
	 * 
	 * @param id
	 */
	public void removePadre(String id) {
		this.padres.remove(id);
	}

	/**
	 * Devuelve la colección de componentes hijos de este componente.
	 * 
	 * @return Coleccion de componentes hijos
	 */
	public Collection<Componente> getComponentes() {
		return null;
	}

	/**
	 * Carga las validaciones para este componente.
	 */
	public void addValidaciones() {
	}

	/**
	 * Valida el componente y devuelve las observaciones correspondientes en el
	 * caso que no sea válido.
	 * 
	 * @return Devuelve <code>null</code> si es válido o las observaciones
	 *         correspondientes si no lo es.
	 */
	public Observacion validar() {
		Observacion observaciones = new Observacion(this);

		for (mereditor.modelo.validacion.Validacion validacion : this.validaciones)
			observaciones.addObservaciones(validacion.validar(this));

		return observaciones;
	}

	/**
	 * Implementación de la evaluación de igualdad por comparación de ids de
	 * componentes.
	 */
	@Override
	public boolean equals(Object obj) {
		if (Componente.class.isInstance(obj))
			return obj == null ? false : this.getId().equals(
					((Componente) obj).getId());
		else
			return false;
	}

	/**
	 * Indica si la instancia de este componente es de la clase que se pasa por
	 * parámetro.
	 * 
	 * @param tipoComponente
	 * @return
	 */
	public boolean es(Class<?> tipoComponente) {
		return tipoComponente.isInstance(this);
	}

	/**
	 * Indica si este componente tiene al especificado por parámetro como hijo.
	 * 
	 * @param componente
	 * @return
	 */
	public boolean contiene(Componente componente) {
		return false;
	}

	/**
	 * Devuelve la lista de objectos filtrados por el tipo que se le pasa.
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> Set<T> filtrarComponentes(Class<T> clazz,
			Collection<Componente> coleccion) {
		Set<T> lista = new HashSet<>();

		for (Componente componente : coleccion)
			if (clazz.isInstance(componente))
				lista.add(clazz.cast(componente));

		return lista;
	}

	@Override
	public int compareTo(Componente componente) {
		return this.toString().compareTo(componente.toString());
	}
}
