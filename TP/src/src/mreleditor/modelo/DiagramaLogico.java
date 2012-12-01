/**
 * 
 */
package mreleditor.modelo;

import java.util.ArrayList;

import mereditor.modelo.Diagrama;
import mereditor.modelo.Proyecto;
import mereditor.modelo.base.ComponenteNombre;

/**
 * @author guido
 * 
 */
public class DiagramaLogico extends ComponenteNombre {
	
	
	private Proyecto proyecto=null;
	protected ArrayList<Tabla> tablas=new ArrayList<Tabla>();
	protected Diagrama der=null;

	
	public DiagramaLogico() {
		
	}
	public DiagramaLogico(Proyecto proyecto) {
		this.setProyecto(proyecto);
	}
	
	public ArrayList<Tabla> getTablas() {
		return tablas;
	}

	public void setTablas(ArrayList<Tabla> tablas) {
		this.tablas = tablas;
	}
	
	public void agregar(Tabla tabla) {
		tabla.setPadre(this);
		this.tablas.add(tabla);
	}
	public Diagrama getDer() {
		return der;
	}
	public void setDer(Diagrama der) {
		this.der = der;
	}
	public Proyecto getProyecto() {
		return proyecto;
	}
	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

}
