package mereditor.interfaz.swt.figuras;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import mereditor.control.DiagramaLogicoControl;
import mereditor.interfaz.swt.listeners.AreaSeleccionControlador;
import mereditor.interfaz.swt.listeners.ArrastreControlador;
import mereditor.interfaz.swt.listeners.ArrastreSeleccionControlador;
import mereditor.interfaz.swt.listeners.MovimientoControlador;
import mereditor.interfaz.swt.listeners.SeleccionControlador;
import mereditor.modelo.Proyecto;
import mreleditor.modelo.DiagramaLogico;

public class DiagramaLogicoFigura extends DiagramaFigure {


	
	/**
	 * Constructor
	 * @param canvas
	 * @param diagrama
	 */
	public DiagramaLogicoFigura(FigureCanvas canvas,Proyecto proyecto) {
		this.proyecto = proyecto;
		this.canvas = canvas;
		this.canvas.setContents(this);
		
		// Agregar el handler que deselecciona todas las figuras cuando se hace
		// click sobre el fondo.
		this.addMouseListener(this.selection);
		// Agregar el controlador de arrastre para que si el cursor se escapa de
		// la figura que se está arrastrando, no se deje de mover dado que toma
		// el control el listener de esta figura.
		new ArrastreSeleccionControlador(this);
		// Agregar el controlador de arrastre para elementos que no se pueden
		// seleccionar.
		new ArrastreControlador(this);
		// Agregar el controlador que permite seleccionar las figuras
		// que se encuentran dentro del área establecida.
		new AreaSeleccionControlador(this);
	}
	
	
	
	
	

	
	
	@Override
	public void add(IFigure figure, Object constraint, int index) {
		super.add(figure, constraint, index);
		// Monitorear los movimientos de la figura hija para cambiar el tamaño
		// en caso de que se vaya de los límites de esta
		figure.addFigureListener(this);
	}

	@Override
	public void remove(IFigure figure) {
		// Dejar de monitorear los movimientos de la figura hija
		figure.removeFigureListener(this);
		super.remove(figure);
	}
	
	@Override
	public void figureMoved(IFigure source) {
		this.checkResizeBounds(source);
	}
	
	
	
	
	/**
	 * Verifica si la figura se encuentra fuera de los límites de esta figura.
	 * Si lo está se expande según sea necesario.
	 * 
	 * @param source
	 */
	private void checkResizeBounds(IFigure source) {
		if (!this.getBounds().contains(source.getBounds())) {
			Rectangle child = source.getBounds();
			Rectangle parent = this.getBounds();
			int top = parent.y - child.y;
			int left = parent.x - child.x;
			int bottom = (child.y + child.height) - parent.height;
			int right = (child.x + child.width) - parent.width;
			top = top > 0 ? top : 0;
			left = left > 0 ? left : 0;
			bottom = bottom > 0 ? bottom : 0;
			right = right > 0 ? right : 0;
			this.setBounds(parent.getExpanded(new Insets(0, 0, bottom, right)));

			if (top > 0 || left > 0) {
				// Suspender el loqueo de las figuras.
				MovimientoControlador.moverLoqueadas(false);
				this.translateChildren(left, top);
				// Suspender el loqueo de las figuras.
				MovimientoControlador.moverLoqueadas(true);
			}
		}
	}

	private void translateChildren(int dx, int dy) {
		for (Object child : this.getChildren()) {
			Figure figure = (Figure) child;

			figure.removeFigureListener(this);
			figure.setBounds(figure.getBounds().getTranslated(dx, dy));
			figure.addFigureListener(this);
		}
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#translateToParent(Translatable)
	 */
	public void translateToParent(Translatable t) {
		t.performScale(zoom);
		super.translateToParent(t);
	}

	/**
	 * @see org.eclipse.draw2d.Figure#translateFromParent(Translatable)
	 */
	public void translateFromParent(Translatable t) {
		super.translateFromParent(t);
		t.performScale(1 / zoom);
	}

	/**
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	protected boolean useLocalCoordinates() {
		return true;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getClientArea()
	 */
	public Rectangle getClientArea(Rectangle rect) {
		super.getClientArea(rect);
		rect.width /= zoom;
		rect.height /= zoom;
		return rect;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintClientArea(Graphics)
	 */
	protected void paintClientArea(Graphics graphics) {
		if (getChildren().isEmpty())
			return;

		boolean optimizeClip = getBorder() == null || getBorder().isOpaque();

		ScaledGraphics g = new ScaledGraphics(graphics);

		if (!optimizeClip)
			g.clipRect(getBounds().getShrinked(getInsets()));
		g.translate(getBounds().x + getInsets().left, getBounds().y + getInsets().top);
		g.scale(zoom);
		g.pushState();
		paintChildren(g);
		g.popState();
		g.dispose();
		graphics.restoreState();
	}
}
