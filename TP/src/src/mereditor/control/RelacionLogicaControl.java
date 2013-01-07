package mereditor.control;

import org.eclipse.draw2d.Figure;

import mereditor.interfaz.swt.figuras.Figura;
import mereditor.interfaz.swt.figuras.RelacionLogicaFigure;
import mreleditor.modelo.Relacion;

public class RelacionLogicaControl extends Relacion implements Control<Relacion>{
	private RelacionLogicaFigure figura;
	
	
	@Override
	public Figura<Relacion> getFigura(String idDiagrama) {
		return this.figura;
	}

	@Override
	public void dibujar(Figure contenedor, String idDiagrama) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getNombreIcono() {
		// TODO Auto-generated method stub
		return null;
	}

}
