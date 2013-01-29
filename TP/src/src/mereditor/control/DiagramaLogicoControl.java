package mereditor.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.draw2d.Figure;

import mereditor.interfaz.swt.figuras.Figura;
import mereditor.modelo.Proyecto;
import mreleditor.modelo.DiagramaLogico;
import mreleditor.modelo.Tabla;

public class DiagramaLogicoControl extends DiagramaLogico implements Control<DiagramaLogico>{

	/*@SuppressWarnings("rawtypes")
	protected ArrayList<Figura> listaObjetosLogicos;*/
	
	//protected ArrayList<TablaControl> listaTablasControl;
	
	public DiagramaLogicoControl(Proyecto proyecto) {
		super(proyecto);
	}
	
	public DiagramaLogicoControl(DiagramaLogico dia) {
		super(dia);
	}
	
	@Override
	public Figura<DiagramaLogico> getFigura(String idDiagrama) {
		return null;
	}
	
	/*private boolean checkTable(String id) {
		Iterator<TablaControl> it = listaTablasControl.iterator();
		while (it.hasNext()) {
			if (id == it.next().getId())
				return true;
		}
		
		return false;
	}*/
	
	

	@Override
	public void dibujar(Figure contenedor, String idDiagrama) {
		idDiagrama = idDiagrama != null ? idDiagrama : this.id;

	/*	if (this.listaTablasControl == null) {
			
			this.listaTablasControl = new ArrayList<TablaControl>();
			
			ArrayList<Tabla> tablas = this.getTablas();
			Iterator<Tabla> it = tablas.iterator();
			
			while (it.hasNext()) {
				this.listaTablasControl.add(new TablaControl(it.next()));
			}
		}
		*/
		this.dibujar(contenedor, idDiagrama, this.tablas);
	}
	
	/*@SuppressWarnings("rawtypes")
	public void setListaObjetosLogicos(ArrayList<Figura> lista) {
		this.listaObjetosLogicos = lista;
	}*/
	
	@SuppressWarnings("rawtypes")
	public ArrayList<Figura> getListaObjetosLogicos() {
		ArrayList<Figura> objetosLogicos= new ArrayList<Figura>();
		
		for(Tabla tabla: tablas){
			objetosLogicos.add(((TablaControl)tabla).getFigura(id));
		}
		return objetosLogicos;
	}
	
	private void dibujar(Figure contenedor, String id, Collection<?> componentes) {
		for (Object componente : componentes)
			((Control<?>) componente).dibujar(contenedor, id);
	}
	
	public void dibujar(Figure contenedor) {
		this.dibujar(contenedor, this.id);
	}
	
	

	@Override
	public String getNombreIcono() {
		return "logicdiagram.png";
	}

}
