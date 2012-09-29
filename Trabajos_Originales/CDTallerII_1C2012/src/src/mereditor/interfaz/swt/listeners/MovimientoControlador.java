package mereditor.interfaz.swt.listeners;

import mereditor.interfaz.swt.figuras.Figura;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * Se encarga de mover las figuras loqueadas de una figura junto con ella al
 * moverse esta.
 */
public class MovimientoControlador implements FigureListener {
	/**
	 * Indica si se deben mover las figuras loqueadas.
	 */
	protected static boolean moverLoqueadas = true;
	protected Figura<?> figura;
	private Point current;

	public MovimientoControlador(Figura<?> figura) {
		this.figura = figura;
		this.current = this.figura.getLocation();
		this.figura.addFigureListener(this);
	}

	/**
	 * Establece si se deben mover las figuras loqueadas.
	 * 
	 * @param mover
	 */
	public static void moverLoqueadas(boolean mover) {
		moverLoqueadas = mover;
	}

	@Override
	public void figureMoved(IFigure movedFigure) {
		if (moverLoqueadas) {
    		Dimension delta = this.figura.getLocation().getDifference(this.current);
    
    		for (Figure figure : this.figura.getFigurasLoqueadas())
    			// Si la figura está seleccionada, la traslación de la misma
    			// se realiza por el controlador DragDrop.
    			if (!SeleccionControlador.isSelected(figure))
    				figure.setBounds(figure.getBounds().getTranslated(delta.width, delta.height));
		}

		this.current = this.figura.getLocation();
	}
}
