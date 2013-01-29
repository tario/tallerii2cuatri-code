package mreleditor.conversor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.draw2d.geometry.Dimension;

import mereditor.control.DiagramaLogicoControl;
import mereditor.control.TablaControl;
import mereditor.interfaz.swt.figuras.Figura;
import mereditor.interfaz.swt.figuras.TablaFigure;
import mereditor.modelo.base.Componente;
import mereditor.representacion.PList;
import mreleditor.modelo.DiagramaLogico;
import mreleditor.modelo.Tabla;

public class ConversorDERRepresentacion {
	
	public final static int defaultFontHeight = Figura.defaultFont.getFontData()[0].getHeight() + 5; // Alto mas espacio entre atributos.
	public final static int defaultFontWidth = Figura.defaultFont.getFontData()[0].getHeight() + 5; // El ancho es aproximado y a eso se le suma el espacio entre letras

	
	/**
	 *  La idea aca es tomando el daigrama logico en memoria, yo creo las figuras y les pongo 
	 *  los datos de su representacion.
	 * @param diagrama
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<Figura> createRepresentation(DiagramaLogicoControl diag) {
		
		ArrayList<Figura> figuras = new ArrayList<Figura>();
	
		int initX = 100;
		int initY = 100;
		
		ArrayList<Tabla> listaTablas = diag.getTablas();
		Iterator<Tabla> it = listaTablas.iterator();
		while (it.hasNext()) {
			Tabla tab = it.next();
			Dimension dim = new Dimension(getMaxStringSize(tab.getAtributos()) * defaultFontWidth, tab.getAtributos().size() * defaultFontHeight);
			TablaControl tabControl =  (TablaControl)tab;
			TablaFigure tFig = new TablaFigure(tabControl, dim);

			
			// Cargo valores que despues se usan para dibujar y los que se guardan en el xml
			PList params = new PList();
			PList pos = new PList();
			pos.set("x", initX);
			pos.set("y", initY);
			params.set("Posicion", pos);
			
			PList pDims = new PList();
			pDims.set("ancho", dim.width);
			pDims.set("alto", dim.height);
			params.set("Dimension", pDims);
			
			tFig.setRepresentacion(params);
			
			initX += dim.width() + 15;
			initY += dim.height() + 15;
			
			figuras.add(tFig);
			tabControl.addFigure(tFig, diag.getId());
		}
		
		return figuras;
	}
	
	private int getMaxStringSize(Set<String> strings) {
		Iterator<String> it = strings.iterator();
		
		int maxSize = 0;
		
		while (it.hasNext()) {
			int auxSize = it.next().length();
			if (auxSize > maxSize)
				maxSize = auxSize;
		}
		
		return maxSize;
		
	}
	
}
