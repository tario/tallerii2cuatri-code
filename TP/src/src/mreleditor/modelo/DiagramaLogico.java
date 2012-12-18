/**
 * 
 */
package mreleditor.modelo;

import java.util.ArrayList;

import mereditor.modelo.Diagrama;
import mereditor.modelo.DiagramaDER;
import mereditor.modelo.Proyecto;
import mereditor.modelo.base.ComponenteNombre;

/**
 * @author guido
 * 
 */
public class DiagramaLogico extends Diagrama {
	
	protected ArrayList<Tabla> tablas=new ArrayList<Tabla>();
	protected DiagramaDER der=null;

	
	public DiagramaLogico() {
		
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
	public DiagramaDER getDer() {
		return der;
	}
	public void setDer(DiagramaDER der) {
		this.der = der;
	}

}
