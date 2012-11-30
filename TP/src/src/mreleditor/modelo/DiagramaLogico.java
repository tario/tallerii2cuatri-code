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
public class DiagramaLogico extends ComponenteNombre {
	
	
	private Proyecto proyecto=null;
	private ArrayList<Tabla> tablas=new ArrayList<Tabla>();
	private ArrayList<Relacion> relaciones= new ArrayList<Relacion>();

	
	public DiagramaLogico() {
		
	}
	public DiagramaLogico(Proyecto proyecto) {
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
