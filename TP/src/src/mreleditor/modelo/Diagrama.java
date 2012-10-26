/**
 * 
 */
package mreleditor.modelo;

import java.util.ArrayList;

/**
 * @author guido
 *
 */
public class Diagrama {
	private ArrayList<Tabla> tablas;
	private ArrayList<Relacion> relaciones;
	public ArrayList<Tabla> getTablas() {
		return tablas;
	}
	public void setTablas(ArrayList<Tabla> tablas) {
		this.tablas = tablas;
	}
	public ArrayList<Relacion> getRelaciones() {
		return relaciones;
	}
	public void setRelaciones(ArrayList<Relacion> relaciones) {
		this.relaciones = relaciones;
	}
	
}
