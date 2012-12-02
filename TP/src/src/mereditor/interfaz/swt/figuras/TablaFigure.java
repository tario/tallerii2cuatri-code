package mereditor.interfaz.swt.figuras;

import org.eclipse.draw2d.geometry.Dimension;

import mereditor.modelo.Entidad;
import mreleditor.modelo.Tabla;

public class TablaFigure extends Figura<Tabla> {

	public TablaFigure(Tabla componente, Dimension dim) {
		super(componente, dim);
		
	}

	public TablaFigure(Tabla tabla) {
		super(tabla);
		
	}

	@Override
	public void actualizar() {
		// TODO Auto-generated method stub

	}

}
