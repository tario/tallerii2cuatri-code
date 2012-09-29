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
 * Se encarga del arrastre de las figuras que se encuentran seleccionadas.
 */
public class ArrastreSeleccionControlador extends MouseMotionListener.Stub implements MouseListener {
	private static Point startPoint;
	private static boolean arrastrando;

	public ArrastreSeleccionControlador(Figure figure) {
		figure.addMouseListener(this);
		figure.addMouseMotionListener(this);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.moverPrimeraPosicion((Figure) e.getSource());
		startPoint = e.getLocation();
		arrastrando = true;		
	}

	/**
	 * Coloca esta figura primera en la colección de hijos para que reciba todos
	 * los eventos primero.
	 * 
	 * @param figure
	 */
	@SuppressWarnings("unchecked")
	private void moverPrimeraPosicion(Figure figure) {
		IFigure parent = figure.getParent();
		List<IFigure> children = parent.getChildren();
		IFigure first = children.get(0);

		int pos = children.indexOf(figure);
		parent.getChildren().set(0, figure);
		parent.getChildren().set(pos, first);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		arrastrando = false;
	}

	@Override
	public void mouseDoubleClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (arrastrando)
			for (IFigure figura : SeleccionControlador.getSelected())
				this.actualizarPosicion((Figure) figura, startPoint, e.getLocation());

		startPoint = e.getLocation().getCopy();
	}

	public void actualizarPosicion(Figure figure, Point src, Point dest) {
		if (dest != null && src != null && !dest.equals(src)) {
			Dimension diff = dest.getDifference(src);
			figure.translate(diff.width, diff.height);
		}
	}
}
