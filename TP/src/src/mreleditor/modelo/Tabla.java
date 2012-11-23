/**
 * 
 */
package mreleditor.modelo;


import java.util.ArrayList;
import java.util.HashMap;

import mereditor.modelo.base.ComponenteNombre;
/**
 * @author guido
 *
 */
public class Tabla extends ComponenteNombre {

	private ArrayList<String> clave_primaria;
	//Primera idea, es un HashMap indexado por nombreTabla de la que es foranea
	private HashMap<String,ArrayList<String> > clave_foranea;
	private ArrayList<String> atributos;
	private ArrayList<Relacion> relaciones;
	private String nombre;
	
	public Tabla() {
		
	}
	
	public Tabla (String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public ArrayList<String> getClave_primaria() {
		return clave_primaria;
	}
	public void setClave_primaria(ArrayList<String> clave_primaria) {
		this.clave_primaria = clave_primaria;
	}
	public HashMap<String,String> getClave_foranea() {
		return clave_foranea;
	}
	//public void setClave_foranea(ArrayList<String> clave_foranea) {
	//	this.clave_foranea = clave_foranea;
	//}
	public ArrayList<String> getAtributos() {
		return atributos;
	}
	public void setAtributos(ArrayList<String> atributos) {
		this.atributos = atributos;
	}
	
	public void setAtributo(String atributo, int posicion) {
		this.atributos.add(posicion, atributo);
	}
	public ArrayList<Relacion> getRelaciones() {
		return relaciones;
	}
	public void setRelaciones(ArrayList<Relacion> relaciones) {
		this.relaciones = relaciones;
	}
	
	public void agregarAtributo(String atributo) {
		this.atributos.add(atributo);
	}
	
	public void addClave_foranea(ArrayList<String> fks, String nombreTabla) {
		ArrayList<String> claves_foraneas = this.clave_foranea.get(nombreTabla);
		if(claves_foraneas == null) {
			this.clave_foranea.put(nombreTabla,claves_foraneas);
		} else {
			for (String fk : fks) {
				claves_foraneas.add(fk);
			}
			this.clave_foranea.put(nombreTabla, claves_foraneas);
		}
		
	}
	
	public void addClave_primaria(ArrayList<String> pks) {
		for (String pk : pks) {
			this.clave_primaria.add(pk);
		}
	}
	
	public void addClave_primaria(String pk) {
		this.clave_primaria.add(pk);
	}
		
}
