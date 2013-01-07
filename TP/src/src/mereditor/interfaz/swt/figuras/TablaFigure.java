package mereditor.interfaz.swt.figuras;

import org.eclipse.draw2d.geometry.Dimension;

import mreleditor.modelo.Tabla;

public class TablaFigure extends Figura<Tabla> {

	protected String id;
	
	public TablaFigure(Tabla componente, Dimension dim) {
		super(componente, dim);
		id = new String(componente.getId());
	}

	public TablaFigure(Tabla tabla) {
		super(tabla);
		
	}

	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	@Override
	public void actualizar() {
		// TODO Auto-generated method stub

	}

}
