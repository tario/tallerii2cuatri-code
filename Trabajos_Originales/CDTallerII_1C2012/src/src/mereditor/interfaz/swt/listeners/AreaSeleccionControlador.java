package mereditor.interfaz.swt.listeners;

import java.util.ArrayList;
import java.util.List;

import mereditor.interfaz.swt.figuras.Figura;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

public class AreaSeleccionControlador extends MouseMotionListener.Stub implements MouseListener {
	private Figure areaSeleccion = new Figure();
	private Figure parent = null;
	private Point origin = null;

	public AreaSeleccionControlador(Figure parent) {
		this.parent = parent;
		this.parent.addMouseListener(this);
		this.parent.addMouseMotionListener(this);
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		if (this.origin != null) {
			this.areaSeleccion.setBounds(new Rectangle(this.origin, me.getLocation()));
		}
	}

	@Override
	public void mousePressed(MouseEvent me) {
		this.origin = me.getLocation().getCopy();

		this.areaSeleccion.setBorder(new LineBorder(Figura.grey, 1, SWT.LINE_DOT));
		Rectangle bounds = new Rectangle();
		bounds.setLocation(this.origin.getCopy());
		this.areaSeleccion.setBounds(bounds);

		this.parent.add(this.areaSeleccion);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		if (this.origin != null) {
			this.origin = null;
			this.selectFiguresInsideArea();
			if (this.areaSeleccion.getParent() == this.parent)
				this.parent.remove(this.areaSeleccion);
		}
	}

	/**
	 * Selecciona las figuras que se encuentran dentro del area.
	 */
	@SuppressWarnings("unchecked")
	private void selectFiguresInsideArea() {
		List<Object> children = new ArrayList<Object>(this.parent.getChildren());
		for (Object child : children) {
			if (child instanceof Figura<?>) {
				Figura<?> figura = (Figura<?>) child;

				Rectangle area = this.areaSeleccion.getBounds();

				if (area.contains(figura.getBounds()))
					SeleccionControlador.select(figura);
			}
		}
	}

	@Override
	public void mouseDoubleClicked(MouseEvent me) {
	}
}
