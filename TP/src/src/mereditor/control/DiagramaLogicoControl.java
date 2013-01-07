package mereditor.control;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.draw2d.Figure;

import mereditor.interfaz.swt.figuras.Figura;
import mereditor.modelo.Proyecto;
import mereditor.modelo.base.Componente;
import mreleditor.modelo.DiagramaLogico;
import mreleditor.modelo.Tabla;

public class DiagramaLogicoControl extends DiagramaLogico implements Control<DiagramaLogico>{

	@SuppressWarnings("rawtypes")
	protected ArrayList<Figura> listaObjetosLogicos;
	
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

	@Override
	public void dibujar(Figure contenedor, String idDiagrama) {
		idDiagrama = idDiagrama != null ? idDiagrama : this.id;

		ArrayList<Tabla> tablas = this.getTablas();
		this.dibujar(contenedor, idDiagrama, tablas);

		
		
	}
	
	@SuppressWarnings("rawtypes")
	public void setListaObjetosLogicos(ArrayList<Figura> lista) {
		this.listaObjetosLogicos = lista;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList<Figura> getListaObjetosLogicos() {
		return this.listaObjetosLogicos;
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
		return "diagrama.png";
	}

}
