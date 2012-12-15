package mereditor.control;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.draw2d.Figure;

import mereditor.interfaz.swt.figuras.Figura;
import mereditor.modelo.Proyecto;
import mreleditor.modelo.DiagramaLogico;
import mreleditor.modelo.Tabla;

public class DiagramaLogicoControl extends DiagramaLogico implements Control<DiagramaLogico>{

	public DiagramaLogicoControl(Proyecto proyecto) {
		super(proyecto);
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
