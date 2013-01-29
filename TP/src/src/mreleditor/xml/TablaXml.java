package mreleditor.xml;

import org.w3c.dom.Element;

import mereditor.control.TablaControl;
import mereditor.xml.Constants;
import mereditor.xml.ParserXML;
import mereditor.xml.Xmlizable;

public class TablaXml extends TablaControl implements Xmlizable {
	
	
	public TablaXml(TablaControl tabla){
		this.id = tabla.getId();
		this.nombre = tabla.getNombre();
		this.clavePrimaria=tabla.getClavePrimaria();
		this.atributos=tabla.getAtributos();
		this.clavesForaneas=tabla.getClavesForaneas();
	}

	public TablaXml() {}

	@Override
	public Element toXml(ParserXML parser_) throws Exception {
		ModeloLogicoParserXml parser=(ModeloLogicoParserXml) parser_;
		
		Element elemento = parser.crearElemento(Constants.TABLA_TAG);
		parser.agregarId(elemento, this.id.toString());
		parser.agregarNombre(elemento, nombre);

		if (this.clavePrimaria.size() > 0) {
			Element pkElement = parser.agregarClavePrimaria(elemento);
			for (String atributo : this.clavePrimaria)
				parser.agregarAtributoClavePrimaria(pkElement,atributo );
		}

		if (this.atributos.size() > 0) {
			Element atributosXml = parser.agregarAtributos(elemento);

			for (String atributo : this.atributos) {
				parser.agregarAtributo(atributosXml,atributo);	
			}
		}
		if (this.clavesForaneas.size() > 0){
			for(ClaveForanea foranea:clavesForaneas){
				Element claveForaneaXml = parser.agregarClaveForanea(elemento);
				parser.agregarTablaReferenciada(claveForaneaXml,foranea.getTablaReferenciada());
				Element atributosXml = parser.agregarAtributos(claveForaneaXml);

				for (String atributo : foranea.getAtributos()) {
					parser.agregarAtributo(atributosXml,atributo);	
				}
			}
		}

		return elemento;
	}

	@Override
	public void fromXml(Element elemento, ParserXML parser_) throws Exception {
		ModeloLogicoParserXml parser=(ModeloLogicoParserXml) parser_;
		
		this.id = parser.obtenerId(elemento);
		this.nombre = parser.obtenerNombre(elemento);

		parser.registrar(this);

		for (String atributo : parser.obtenerAtributos(elemento)) {
			this.atributos.add(atributo);
		}
		for (String pk : parser.obtenerClavePrimaria(elemento)) {
			this.clavePrimaria.add(pk);
		}
		for (ClaveForanea fk : parser.obtenerClaveForanea(elemento,this)) {
			this.clavesForaneas.add(fk);
		}

	}

}
