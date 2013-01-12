package mereditor.interfaz.swt.figuras;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import mereditor.modelo.Entidad;
import mereditor.modelo.Entidad.TipoEntidad;
import mreleditor.modelo.Tabla;

public class TablaFigure extends Figura<Tabla> {

	protected String id;
	
	public TablaFigure(Tabla componente, Dimension dim) {
		super(componente, dim);
		id = new String(componente.getId());
	}

	public TablaFigure(Tabla tabla) {
		super(tabla);
		// uso el mismo tipo de estilo que las entidades... ni ganas de hacer uno nuevo.
		this.setRepresentacion(EstilosFiguras.get(Entidad.class, TipoEntidad.MAESTRA_COSA));
	}
	
	@Override
	protected void init() {
		super.init();

		this.actualizar();
	}
	
	public Connection conectarTabla(Figura<Tabla> figura) {
		Connection conexion = Figura.conectarChopboxEllipse(this, figura);

		this.getParent().add(conexion);
		this.conexiones.put(figura.componente.getId(), conexion);

		return conexion;
	}
	
	public Connection conectarEntidad(String id, Connection conexionEntidad) {
		Ellipse circuloConexion = this.circuloIdentificador();
		conexionEntidad.add(circuloConexion, new MidpointLocator(conexionEntidad, 0));

		Connection conexion = Figura.conectarChopbox(this, circuloConexion);
		this.getParent().add(conexion);
		this.conexiones.put(id, conexion);

		return conexion;
	}


	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	@Override
	public void actualizar() {
		this.lblName.setText(this.componente.getNombre());
	}
	
	private Ellipse circuloIdentificador() {
		Color negro = new Color(null, 0, 0, 0);
		Ellipse circulo = new Ellipse();
		circulo.setAntialias(SWT.ON);
		circulo.setSize(10, 10);
		circulo.setBackgroundColor(negro);
		return circulo;
	}

}
