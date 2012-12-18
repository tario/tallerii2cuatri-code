package mereditor.interfaz.swt.figuras;

import java.util.LinkedHashMap;
import java.util.Map;

import mereditor.control.Control;
import mereditor.control.DiagramaDERControl;
import mereditor.interfaz.swt.listeners.SeleccionControlador;
import mereditor.modelo.Proyecto;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

public abstract class DiagramaFigure extends Figure implements FigureListener{

	protected FigureCanvas canvas;
	protected Proyecto proyecto;
	
	/**
	 * Valores disponibles de zoom.
	 */
	public final static Map<String, Float> zoomOptions = new LinkedHashMap<>();
	public final static String zoom100 = "100%";

	public static final float MIN_ZOOM = 0.25f;
	public static final float MAX_ZOOM = 2.0f;
	public static final float DELTA_ZOOM = 0.25f;
	
	protected float zoom = 1;
	
	/**
	 * Listener que deselecciona todas las figuras que están seleccionadas al
	 * hacer un click sobre el fondo.
	 */
	protected MouseListener selection = new MouseListener.Stub() {
		public void mousePressed(MouseEvent me) {
			SeleccionControlador.deselectAll(null);
		};
	};

	static {
		initZoomOptions();
	}

	/**
	 * Inicializa el mapa que ofrece los diferentes valores de zoom disponibles.
	 */
	protected static void initZoomOptions() {
		float zoom = 0;
		while (zoom < DiagramaDERFigura.MAX_ZOOM) {
			zoom += DiagramaDERFigura.DELTA_ZOOM;
			if (zoom >= DiagramaDERFigura.MIN_ZOOM) {
				String key = Integer.toString((int) (zoom * 100));
				zoomOptions.put(key + "%", zoom);
			}
		}
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

	/**
	 * Establece un valor zoom determinado.
	 * 
	 * @param zoom
	 *            Debe ser alguno de los valores establecidos en
	 *            {@link #zoomOptions}.
	 */
	public void zoom(String zoom) {
		if (zoomOptions.containsKey(zoom))
			this.setZoom(zoomOptions.get(zoom));
		else {
			float zoomFloat = Float.parseFloat(zoom.replace("%", ""));
			this.setZoom(zoomFloat);
		}
	}

	/**
	 * Establecer el nuevo zoom y volver a dibujar la figura.
	 * 
	 * @param zoom
	 */
	public void setZoom(float zoom) {
		this.zoom = zoom;
		revalidate();
		repaint();
	}
	/**
	 * Devuelve un string con el porecentaje de zoom actual y el símbolo % al
	 * final.
	 * 
	 * @return Ejemplo: 100%
	 */
	public String getZoom() {
		String zoom = Integer.toString((int) (this.zoom * 100));
		return zoom + "%";
	}
	
	
	/**
	 * Actualiza la vista con el diagrama actual.
	 */
	public void actualizar() {
		this.removeAll();
		Control<?> diagrama = (Control<?>) this.proyecto.getDiagramaActual();
		String id =proyecto.getDiagramaActual().getId();
		diagrama.dibujar(this,id);
	}
	
	/**
	 * Devuelve un thumbnail del diagrama actual.
	 * 
	 * @return
	 */
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

}
