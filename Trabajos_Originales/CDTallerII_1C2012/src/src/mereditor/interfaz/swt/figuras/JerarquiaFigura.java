package mereditor.interfaz.swt.figuras;

import java.util.ArrayList;
import java.util.List;

import mereditor.modelo.Jerarquia;
import mereditor.representacion.PList;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;

public class JerarquiaFigura extends Figura<Jerarquia> implements
		FigureListener {
	/**
	 * Lista de figuras que forman parte de esta jerarquia.
	 */
	private List<Figure> figuras = new ArrayList<>();
	/**
	 * Tamaño de la figura en la que convergen los conectores de las figuras de
	 * la entidad generica y derivadas.
	 */
	private static final int size = 0;

	public JerarquiaFigura(Jerarquia componente) {
		super(componente);
		this.setRepresentacion(EstilosFiguras.get(Jerarquia.class,
				this.componente.getTipo()));
	}

	@Override
	protected void init() {
		super.init();
		this.setSize(new Dimension(size, size));
	}

	/**
	 * Se establece un tamaño fijo de representación.
	 */
	@Override
	public PList getRepresentacion() {
		PList repr = super.getRepresentacion();
		repr.<PList> get("Dimension").set("ancho", size);
		repr.<PList> get("Dimension").set("alto", size);

		return repr;
	}

	/**
	 * Conectar con la figura de la entidad genérica.
	 * 
	 * @param generica
	 */
	public void conectarGenerica(Figure generica) {
		this.resetFiguras();
		this.figuras.add(generica);
		generica.addFigureListener(this);

		PolylineConnection connection = (PolylineConnection) Figura
				.conectarChopbox(this, generica);
		this.applyLineStyle(connection);

		// Generar el estilo de la flecha y la flecha
		PolygonDecoration flecha = new PolygonDecoration();
		flecha.setAntialias(SWT.ON);
		PointList puntos = new PointList();
		puntos.addPoint(0, 0);
		puntos.addPoint(-2, 1);
		puntos.addPoint(-2, -1);
		flecha.setTemplate(puntos);

		connection.setTargetDecoration(flecha);

		this.getParent().add(connection);
	}

	/**
	 * Conectar con la figura de una entidad derivada.
	 * 
	 * @param derivada
	 */
	public void conectarDerivada(Figure derivada) {
		this.figuras.add(derivada);
		derivada.addFigureListener(this);

		PolylineConnection connection = (PolylineConnection) Figura
				.conectarChopbox(this, derivada);
		connection.setLineStyle(this.lineStyle);

		this.getParent().add(connection);

		// Actualizar posición
		this.figureMoved(derivada);
	}

	/**
	 * Desasocia los listeners de las figuras conectadas con esta y vacía la
	 * lista.
	 */
	protected void resetFiguras() {
		if (this.figuras != null) {
			for (Figure figure : this.figuras)
				figure.removeFigureListener(this);
		
			this.figuras.clear();
		}		
	}

	@Override
	public void actualizar() {

	}

	@Override
	public void figureMoved(IFigure source) {
		Point location = new Point();

		for (Figure figure : this.figuras) {
			location.x += figure.getLocation().x + figure.getSize().width / 2;
			location.y += figure.getLocation().y;
		}

		location.x /= this.figuras.size();
		location.y /= this.figuras.size();

		this.setLocation(location);
	}
}
