package mereditor.interfaz.swt.figuras;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import mereditor.control.DiagramaLogicoControl;
import mereditor.control.TablaControl;
import mereditor.modelo.Entidad;
import mereditor.modelo.Entidad.TipoEntidad;
import mreleditor.modelo.DiagramaLogico;
import mreleditor.modelo.Tabla;
import mreleditor.modelo.Tabla.ClaveForanea;

public class TablaFigure extends Figura<Tabla> {

	protected String id;	
	
	private AtributosFigure atributos;
	
	private static int COMMON_PADDING = 30;
	private static int ATRIBUTE_PIXEL_HEIGHT = 20;
	private static int CHARACTER_PIXEL_WIDTH = 6;
	
	public TablaFigure(Tabla componente, Dimension dim) {
		super(componente, dim);
		id = new String(componente.getId());
	}

	public TablaFigure(Tabla tabla) {
		super(tabla);
		id = new String(tabla.getId());
		atributos = new AtributosFigure();
		this.setRepresentacion(EstilosFiguras.get(Entidad.class, TipoEntidad.MAESTRA_COSA));
	}
	
	@Override
	protected void init() {
		super.init();
		
		ToolbarLayout layout = new ToolbarLayout();
	    setLayoutManager(layout);	
		
		this.remove(lblName); // remove it from 'CENTER'
		this.add(lblName, BorderLayout.TOP); // add it at the 'TOP'	
		this.lblName.setText(this.componente.getNombre());
		
		atributos = new AtributosFigure();
		this.add(atributos);
		addAttributesLabels();
		
		//this.actualizar(); 
	}
	
	public Connection conectarTabla(Figura<Tabla> figura) {
		Connection conexion = Figura.conectarChopboxEllipse(this, figura);

		//this.getParent().add(conexion); // lanza nullreference exception, getParent()==null
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

	private void calculateRectangleDimention() {
		// Calcular alto de rectangulo segun cantidad de atributos de la tabla + padding top-bottom
		Tabla tabla = this.componente;
		int iCantidadAtributos = tabla.getAtributos().size();
		int iRectangleHeight = (iCantidadAtributos * ATRIBUTE_PIXEL_HEIGHT) + COMMON_PADDING;
		
		// Calcular ancho de rectangulo segun nombres de atributos de la tabla + padding left-right
		Iterator<String> it = tabla.getAtributos().iterator();
		int iMaxAtributoName = 0;
		while( it.hasNext() ) {
			String sAtributoName = it.next();
			if( sAtributoName.length() > iMaxAtributoName ) iMaxAtributoName = sAtributoName.length();
		}
		
		if( iMaxAtributoName < tabla.getNombre().length() ) iMaxAtributoName = tabla.getNombre().length();
		
		int iRectangleWidth = ( (2 * COMMON_PADDING) + (iMaxAtributoName * CHARACTER_PIXEL_WIDTH) );
		
		this.setSize( iRectangleWidth, iRectangleHeight );
	}
	
	private void addAttributesLabels() {
		Tabla tabla = this.componente;
		
		Set<String> atributosPK = tabla.getClavePrimaria();
		Iterator<String> it = tabla.getAtributos().iterator();
		while( it.hasNext() ) {
			
			// Incluir PK o FK segun se trate de claves primarias o foraneas
			String attribute = "";
			boolean bIsPK = false;
			attribute = it.next();
			if( atributosPK.contains(attribute) ) {
				attribute = "PK - " + attribute;
				bIsPK = true;
			} 
			
			if( !bIsPK && !tabla.getClavesForaneas().isEmpty() ) {
				Iterator<ClaveForanea> itFK = tabla.getClavesForaneas().iterator();
				while( itFK.hasNext() ) {
					Set<String> atributosFK = itFK.next().getAtributos();
					if( atributosFK.contains(attribute)) {
						attribute = "FK - " + attribute;
					}
				}
			} 

			Label newLabel = new Label();
			newLabel.setFont( this.getFont() );
			newLabel.setText( attribute );
			
			this.atributos.add(newLabel);
		}
	}
	
	private void drawTableFKRelations() {
		TablaControl tablaC = (TablaControl)this.componente;
		
		Iterator<ClaveForanea> it = tablaC.getClavesForaneas().iterator();
		while( it.hasNext() ) {
			ClaveForanea claveFK = it.next();
			
			String tablaReferenciada = claveFK.getTablaReferenciada();
			
			DiagramaLogicoControl derC = (DiagramaLogicoControl)tablaC.getPadre();
			TablaControl tablaRef = (TablaControl)derC.getTablaByName(tablaReferenciada);
			TablaFigure figuraTablaRef = (TablaFigure) tablaRef.getFigura(tablaRef.getId());
			
			conectarTabla(figuraTablaRef);
		}
	}
	
	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	@Override
	public void actualizar() {
		Tabla tabla = this.componente;
		
		// Recalcular tamaño de tabla (si se agregaron o quitaron atributos)
		calculateRectangleDimention();
		
		// Nombre de tabla
		this.lblName.setText(tabla.getNombre());
		
		// Redibujar labels de atributos (¿?)
		//addAttributesLabels();	
		
		// Redibujar relaciones con otras tablas
		//drawTableFKRelations();
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
