/**
 * 
 */
package mreleditor.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mereditor.modelo.Diagrama;
import mereditor.modelo.Proyecto;
import mereditor.modelo.base.Componente;
import mereditor.modelo.base.ComponenteNombre;

/**
 * @author guido
 * 
 */
public class DiagramaLogico extends ComponenteNombre {
	
	
	private Proyecto proyecto=null;
	protected ArrayList<Tabla> tablas = new ArrayList<Tabla>();
	protected Diagrama der=null;

	
	public DiagramaLogico() {
		
	}
	
	public DiagramaLogico(DiagramaLogico dia) {
		super();
		this.id=dia.id;
		if (dia.getDer() != null)
			this.setDer(dia.getDer());
		
		this.setNombre(dia.getNombre());
		
		if (dia.getPadre() != null)
			this.setPadre(dia.getPadre());
		
		if (dia.getProyecto() != null)
			this.setProyecto(dia.getProyecto());
		
		if (dia.getTablas() != null)
			this.setTablas(dia.getTablas());
	}
	
	public DiagramaLogico(Proyecto proyecto) {
		this.setProyecto(proyecto);
	}
	
	public ArrayList<Tabla> getTablas() {
		return tablas;
	}

	public Tabla getTablaByName(String nombre) {
		Iterator<Tabla> it = this.tablas.iterator();
		while( it.hasNext() ) {
			Tabla tabla = it.next();
			if( tabla.getNombre().equals(nombre) ) 
				return tabla; 
		}
		return null;
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
