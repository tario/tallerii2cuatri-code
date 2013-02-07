package mereditor.interfaz.swt.figuras;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import mereditor.control.DiagramaLogicoControl;
import mereditor.control.TablaControl;
import mereditor.interfaz.swt.Principal;
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
	}
	
	public Connection conectarTabla(Figura<Tabla> figura) {
		PolylineConnection conexion = (PolylineConnection)Figura.conectarChopbox(this, figura);
		
		PolygonDecoration sourceDecoration = new PolygonDecoration();
		PointList points = new PointList();
		points.addPoint(0,0); points.addPoint(-2,2);
		points.addPoint(-4,0); points.addPoint(-2,-2); points.addPoint(0,0);
		sourceDecoration.setTemplate(points);
		sourceDecoration.setBackgroundColor(ColorConstants.black);
		sourceDecoration.setFill(true);
		sourceDecoration.setScale(3, 3);
		conexion.setSourceDecoration(sourceDecoration);

		conexion.setTargetDecoration(new PolygonDecoration());
		
		this.getParent().add(conexion); 
		this.conexiones.put(figura.componente.getId(), conexion);
		
		return conexion;
	}
	
	private void addAttributesLabels() {
		Tabla tabla = this.componente;
		
		// Agregar Atributos PK
		if( !tabla.getClavePrimaria().isEmpty() ) {
			Iterator<String> itPK = tabla.getClavePrimaria().iterator();
			while( itPK.hasNext() ) {
				String attribute = itPK.next();
				
				Label newLabel = new Label();
				newLabel.setFont( this.getFont() );
				newLabel.setText( attribute );
				newLabel.setIcon( Principal.getIcono("PrimaryKey.png") );
				newLabel.setIconAlignment(PositionConstants.LEFT);
				this.atributos.add(newLabel);
			}
		}
		
		// Agregar Atributos FK
		if( !tabla.getClavesForaneas().isEmpty() ) {
			Iterator<ClaveForanea> itClaveForanea = tabla.getClavesForaneas().iterator();
			while( itClaveForanea.hasNext() ) {
				Iterator<String> itFK = itClaveForanea.next().getAtributos().iterator();
				while( itFK.hasNext() ) {
					String attribute = itFK.next();
					
					Label newLabel = new Label();
					newLabel.setFont( this.getFont() );
					newLabel.setText( attribute );
					newLabel.setIcon( Principal.getIcono("ForeignKey.png") );
					newLabel.setIconAlignment(PositionConstants.LEFT);
					
					this.atributos.add(newLabel);
				}
			}
		}
		
		// Agrego Resto de Atributos
		Iterator<String> it = tabla.getAtributos().iterator();
		while( it.hasNext() ) {
			
			// Incluir PK o FK segun se trate de claves primarias o foraneas
			String attribute = it.next();
			boolean bIsPK = false;
			boolean bIsFK = false;
			if( tabla.getClavePrimaria().contains(attribute) ) {
				bIsPK = true;
			} 
			
			if( !bIsPK && !tabla.getClavesForaneas().isEmpty() ) {
				Iterator<ClaveForanea> itFK = tabla.getClavesForaneas().iterator();
				while( itFK.hasNext() ) {
					Set<String> atributosFK = itFK.next().getAtributos();
					if( atributosFK.contains(attribute)) {
						bIsFK = true;
						break;
					}
				}
			} 

			if( !bIsPK && !bIsFK ) {
				Label newLabel = new Label();
				newLabel.setFont( this.getFont() );
				newLabel.setText( attribute );
				
				this.atributos.add(newLabel);
			}
		}
	}
	
	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
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
	
	@Override
	public void actualizar() {
		Tabla tabla = this.componente;
		
		// Nombre de tabla
		this.lblName.setText(tabla.getNombre());
		
		// Recalcular tamaño de tabla (si se agregaron o quitaron atributos)
		calculateRectangleDimention();
	}

}
