package mreleditor.xml;

import org.w3c.dom.Element;

import mereditor.control.DiagramaLogicoControl;
import mereditor.modelo.Diagrama;
import mereditor.modelo.Proyecto;
import mereditor.modelo.base.Componente;
import mereditor.xml.Constants;
import mereditor.xml.ParserXML;
import mereditor.xml.XmlHelper;
import mereditor.xml.Xmlizable;
import mreleditor.modelo.Tabla;

public class DiagramaLogicoXml extends DiagramaLogicoControl implements Xmlizable {
	
	public DiagramaLogicoXml(Proyecto proyecto, DiagramaLogicoControl componente){
		super(proyecto);
		this.id = componente.getId();
		this.nombre = componente.getNombre();
		this.tablas = componente.getTablas();
		this.der=componente.getDer();
	}


	public DiagramaLogicoXml(Proyecto proyecto) {
		super(proyecto);
	}

	@Override
	public Element toXml(ParserXML parser_) throws Exception {
		ModeloLogicoParserXml parser=(ModeloLogicoParserXml) parser_;
		
		Element elemento = parser.crearElemento(Constants.DIAGRAMA_LOGICO_TAG);
		parser.agregarId(elemento, this.id.toString());
		parser.agregarNombre(elemento, nombre);
		parser.agregarDer(elemento,this.getDer().getId());

		// Agregar las referencias a las tablas
		if (this.tablas.size() > 0) {
			Element tablasElement = parser.agregarTablas(elemento);
			for (Componente tabla : this.tablas) {
				parser.agregarTabla(tablasElement, ((Tabla)tabla).getId());
			}
		}


		return elemento;
	}

	@Override
	public void fromXml(Element elemento, ParserXML parser_) throws Exception {
		ModeloLogicoParserXml parser=(ModeloLogicoParserXml) parser_;
		
		this.id = elemento.getAttribute(Constants.ID_ATTR);
		this.nombre = XmlHelper.querySingle(elemento, Constants.NOMBRE_TAG).getTextContent();
		this.der= (Diagrama) getProyecto().getComponente(XmlHelper.querySingle(elemento, Constants.DER_TAG).getTextContent());
		parser.registrar(this);

		// Tablas
		for (Tabla tabla : parser.obtenerTablas(elemento)) {
			this.agregar(tabla);
		}

	}

}
