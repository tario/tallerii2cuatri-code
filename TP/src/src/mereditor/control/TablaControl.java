package mereditor.control;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.Figure;

import mereditor.interfaz.swt.figuras.TablaFigure;
import mereditor.interfaz.swt.figuras.Figura;
import mreleditor.modelo.Tabla;

public class TablaControl extends Tabla implements Control<Tabla>{
	protected Map<String, TablaFigure> figures = new HashMap<>();
	
	@Override
	public Figura<Tabla> getFigura(String idDiagrama) {
		if (!this.figures.containsKey(idDiagrama)) {
			TablaFigure figura = new TablaFigure(this);
			this.figures.put(idDiagrama, figura);
			
			this.setPosicionInicial(figura);
		}
		
		this.figures.get(idDiagrama).actualizar();

		return this.figures.get(idDiagrama);
	}

	/**
	 * Establecer la posición cuando es una nueva figura.
	 * @param figura
	 */
	private void setPosicionInicial(Figure figura) {
		figura.setBounds(figura.getBounds().getTranslated(100, 100));		
	}

	@Override
	public void dibujar(Figure contenedor, String idDiagrama) {
		TablaFigure figuraTabla = (TablaFigure) this.getFigura(idDiagrama);
		contenedor.add(figuraTabla);

	}

	public Map<String, TablaFigure> getFiguras() {
		return this.figures;
	}
	
	@Override
	public String getNombreIcono() {
		return "tabla.png";
	}


}
