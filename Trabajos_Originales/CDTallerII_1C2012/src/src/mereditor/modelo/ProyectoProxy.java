package mereditor.modelo;

import java.util.Collection;
import java.util.Set;

public interface ProyectoProxy {
	/**
	 * Devuelve todas la entidades del diagrama actual y de sus ancestros.
	 * 
	 * @return
	 */
	public Set<Entidad> getEntidadesDisponibles();

	/**
	 * Devuelve las entidades del diagrama actual.
	 * 
	 * @return
	 */
	public Set<Entidad> getEntidadesDiagrama();

	/**
	 * Devolver una colección con todos los atributos de los componentes del
	 * diagrama actual.
	 * 
	 * @return
	 */
	public Collection<Atributo> getAtributosDiagrama();

	/**
	 * Devuelve todas la Relaciones del diagrama actual y de sus ancestros.
	 * 
	 * @return
	 */
	public Set<Relacion> getRelacionesDisponibles();
	
	/**
	 * Devuelve las Relaciones del diagrama actual.
	 * 
	 * @return
	 */
	public Set<Relacion> getRelacionesDiagrama();
	
	/**
	 * Devuelve todas la Jerarquias del diagrama actual y de sus ancestros.
	 * 
	 * @return
	 */
	public Set<Jerarquia> getJerarquiasDisponibles();
	
	/**
	 * Devuelve las Jerarquias del diagrama actual.
	 * 
	 * @return
	 */
	public Set<Jerarquia> getJerarquiasDiagrama();
}