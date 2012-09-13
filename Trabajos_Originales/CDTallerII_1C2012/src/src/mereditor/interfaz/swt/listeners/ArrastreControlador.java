package mereditor.interfaz.swt.listeners;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * Se encarga del arrastre de una figura que no puede ser seleccionada.
 */
public class ArrastreControlador extends MouseMotionListener.Stub implements MouseListener {
	private static Point startPoint;
	private static IFigure figura; 

	public ArrastreControlador(Figure figure) {
		figure.addMouseListener(this);
		figure.addMouseMotionListener(this);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		figura = (IFigure) e.getSource();
		this.moverPrimeraPosicion(figura);
		startPoint = e.getLocation();
	}

	/**
	 * Coloca esta figura primera en la colección de hijos para que reciba todos
	 * los eventos primero.
	 * 
	 * @param figure
	 */
	@SuppressWarnings("unchecked")
	private void moverPrimeraPosicion(IFigure figure) {
		IFigure parent = figure.getParent();
		List<IFigure> children = parent.getChildren();
		IFigure first = children.get(0);

		int pos = children.indexOf(figure);
		parent.getChildren().set(0, figure);
		parent.getChildren().set(pos, first);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		figura = null;
	}

	@Override
	public void mouseDoubleClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(figura != null)
			this.actualizarPosicion(figura, startPoint, e.getLocation());

		startPoint = e.getLocation().getCopy();
	}

	public void actualizarPosicion(IFigure figure, Point src, Point dest) {
		if (dest != null && src != null && !dest.equals(src)) {
			Dimension diff = dest.getDifference(src);
			figure.setBounds(figure.getBounds().getTranslated(diff.width, diff.height));
		}
	}
}
