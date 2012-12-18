package mereditor.interfaz.swt.figuras;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.draw2d.geometry.Dimension;

import mreleditor.modelo.Tabla;
import mreleditor.modelo.Tabla.ClaveForanea;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;

public class TablaFigure extends Figura<Tabla> {
	
	private RectangleFigure rectangle; // recuadro de la tabla
	private Set<Label> labels; // lista de atributos
	
	private static int PIXEL_PADDING = 10;
	private static int ATRIBUTE_PIXEL_HEIGHT = 20;
	
	public TablaFigure(Tabla componente, Dimension dim) {
		super(componente, dim);
	}

	public TablaFigure(Tabla tabla) {
		super(tabla);
	}
	
	@Override
	public void init() {
		Tabla tabla = this.componente;
		labels=new HashSet<Label>();
		this.lblName = new Label();
		this.lblName.setFont(this.getFont());
		this.lblName.setText(tabla.getNombre());
		this.lblName.setBounds(this.lblName.getTextBounds().getTranslated(0, -10));
		
		this.rectangle = new RectangleFigure();
		this.rectangle.setLocation(this.getLocation());
		
		// Calcular dimensiones de rectangulo segun cantidad de atributos y tamaño de sus atributos
		calculateRectangleDimention(); 
		
		this.setBounds( this.rectangle.getBounds() );
		this.add( this.rectangle );
		
		// Dibujar labels de atributos
		insertAttributesLabels();
		
		//TODO: conectar la tabla con cada tabla relacionada (FK) -usar atributosFK-
		
		this.applyLineStyle(); // ?
	}

	private void calculateRectangleDimention() {
		// Calcular alto de rectangulo segun cantidad de atributos de la tabla + padding top-bottom
		Tabla tabla = this.componente;
		int iCantidadAtributos = tabla.getAtributos().size();
		int iRectangleHeight = iCantidadAtributos * ATRIBUTE_PIXEL_HEIGHT + 2 * PIXEL_PADDING;
		
		// Calcular ancho de rectangulo segun nombres de atributos de la tabla + padding left-right
		Iterator<String> it = tabla.getAtributos().iterator();
		int iRectangleWidth = 0;
		while( it.hasNext() ) {
			String sAtributoName = it.next();
			if( sAtributoName.length() > iRectangleWidth ) iRectangleWidth = sAtributoName.length();
		}
		iRectangleWidth += ( 2 * PIXEL_PADDING );
		
		this.rectangle.setSize( iRectangleWidth, iRectangleHeight );
	}
	
	private void insertAttributesLabels() {
		Tabla tabla = this.componente;
		
		this.labels.clear(); // borrar atributos existentes
		Set<String> atributosPK = tabla.getClavePrimaria(); // si el atributo existe en la lista, lo marcamos como 'PK'
		Set< ClaveForanea > atributosFK = tabla.getClavesForaneas(); // si el atributo existe en la lista, lo marcamos como 'FK' (sii no es PK)
		Iterator<String> it = tabla.getAtributos().iterator();
		int attrNum = 0;
		while( it.hasNext() ) {
			//TODO: indicar primary keys y foreign keys con 'PK' o 'FK'
			
			Label newLabel = new Label();
			newLabel.setFont( this.getFont() );
			newLabel.setText( it.next() );
			newLabel.setBounds( newLabel.getTextBounds().getTranslated( 0, ATRIBUTE_PIXEL_HEIGHT * attrNum ) );
			
			labels.add( newLabel );
			attrNum++;
		}
	}
	
	@Override
	public void actualizar() {
		Tabla tabla = this.componente;
		
		// Nombre de tabla
		this.lblName.setText(tabla.getNombre());
		this.lblName.setBounds(this.lblName.getTextBounds().getTranslated(0, -10));
		
		// Recalcular tamaño de tabla (si se agregaron o quitaron atributos)
		calculateRectangleDimention();
		this.setBounds( this.rectangle.getBounds() );
		
		// Redibujar labels de atributos
		insertAttributesLabels();
	}

}

