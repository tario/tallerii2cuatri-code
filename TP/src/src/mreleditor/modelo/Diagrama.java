/**
 * 
 */
package mreleditor.modelo;

import java.util.ArrayList;

import mereditor.modelo.Proyecto;
import mereditor.modelo.base.ComponenteNombre;

/**
 * @author guido
 * 
 */
public class Diagrama extends ComponenteNombre {
	
	
	private Proyecto proyecto;
	private ArrayList<Tabla> tablas;
	private ArrayList<Relacion> relaciones;

	
	public Diagrama() {
		
	}
	public Diagrama(Proyecto proyecto) {
		this.proyecto = proyecto;
	}
	
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
	
	public void agregar(Tabla tabla) {
		this.tablas.add(tabla);
	}

}
