package mereditor.interfaz.swt.figuras;

import java.util.LinkedHashMap;
import java.util.Map;

import mereditor.control.DiagramaControl;
import mereditor.control.DiagramaLogicoControl;
import mereditor.interfaz.swt.listeners.AreaSeleccionControlador;
import mereditor.interfaz.swt.listeners.ArrastreControlador;
import mereditor.interfaz.swt.listeners.ArrastreSeleccionControlador;
import mereditor.interfaz.swt.listeners.MovimientoControlador;
import mereditor.interfaz.swt.listeners.SeleccionControlador;
import mereditor.modelo.Proyecto;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

public class DiagramaLogicoFigura extends Figure implements FigureListener {

	public final static Map<String, Float> zoomOptions = new LinkedHashMap<>();
	public final static String zoom100 = "100%";

	public static final float MIN_ZOOM = 0.25f;
	public static final float MAX_ZOOM = 2.0f;
	public static final float DELTA_ZOOM = 0.25f;

	private FigureCanvas canvas;
	private Proyecto proyecto;

	private float zoom = 1;
	
	private MouseListener selection = new MouseListener.Stub() {
		public void mousePressed(MouseEvent me) {
			SeleccionControlador.deselectAll(null);
		};
	};

	static {
		initZoomOptions();
	}
	
	private static void initZoomOptions() {
		float zoom = 0;
		while (zoom < DiagramaFigura.MAX_ZOOM) {
			zoom += DiagramaFigura.DELTA_ZOOM;
			if (zoom >= DiagramaFigura.MIN_ZOOM) {
				String key = Integer.toString((int) (zoom * 100));
				zoomOptions.put(key + "%", zoom);
			}
		}
	}
	
	public DiagramaLogicoFigura(FigureCanvas canvas, Proyecto proyecto) {
		this.canvas = canvas;
		this.canvas.setContents(this);
		this.proyecto = proyecto;

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
	 * Actualiza la vista con el diagrama actual.
	 */
	public void actualizar() {
		this.removeAll();
		DiagramaLogicoControl diagrama = this.proyecto.getDiagramaLogicoControl();
		diagrama.dibujar(this);
	}

	/**
	 * Aplica una disminución al zoom.
	 */
	public void zoomOut() {
		if (this.zoom - DELTA_ZOOM >= MIN_ZOOM)
			this.setZoom(this.zoom - DELTA_ZOOM);
	}

	/**
	 * Aplica un aumento al zoom.
	 */
	public void zoomIn() {
		if (this.zoom + DELTA_ZOOM <= MAX_ZOOM)
			this.setZoom(this.zoom + DELTA_ZOOM);
	}
	
	public void zoom(String zoom) {
		if (zoomOptions.containsKey(zoom))
			this.setZoom(zoomOptions.get(zoom));
		else {
			float zoomFloat = Float.parseFloat(zoom.replace("%", ""));
			this.setZoom(zoomFloat);
		}
	}
	
	public void setZoom(float zoom) {
		this.zoom = zoom;
		revalidate();
		repaint();
	}
	
	public Image getImagen() {
		Rectangle rootFigureBounds = this.getBounds();
		GC figureCanvasGC = new GC(this.canvas);

		Image image = new Image(this.canvas.getDisplay(), rootFigureBounds.width,
				rootFigureBounds.height);
		GC imageGC = new GC(image);

		imageGC.setBackground(figureCanvasGC.getBackground());
		imageGC.setForeground(figureCanvasGC.getForeground());
		imageGC.setFont(figureCanvasGC.getFont());
		imageGC.setLineStyle(figureCanvasGC.getLineStyle());
		imageGC.setLineWidth(figureCanvasGC.getLineWidth());
		// imageGC.setXORMode(figureCanvasGC.getXORMode());

		Graphics imgGraphics = new SWTGraphics(imageGC);
		this.paint(imgGraphics);

		return image;
	}
	
	public String getZoom() {
		String zoom = Integer.toString((int) (this.zoom * 100));
		return zoom + "%";
	}
	
	public void translateToParent(Translatable t) {
		t.performScale(zoom);
		super.translateToParent(t);
	}
	
	public void translateFromParent(Translatable t) {
		super.translateFromParent(t);
		t.performScale(1 / zoom);
	}

	protected boolean useLocalCoordinates() {
		return true;
	}

	public Rectangle getClientArea(Rectangle rect) {
		super.getClientArea(rect);
		rect.width /= zoom;
		rect.height /= zoom;
		return rect;
	}

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
