package mereditor.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public abstract class ParserXML {
	
	protected Element root;
	protected DocumentBuilder docBuilder;
	
	
	ParserXML() throws Exception{
		this.docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
	}
	public abstract Document generarXml() throws Exception;
	public abstract Object parsearXml() throws Exception;
	
	void setRoot(String path) throws SAXException, IOException{
		File source = new File(path);
		this.root = docBuilder.parse(source).getDocumentElement();
	}
	
	/**
	 * Crea un elemento utilizando el doc del elemento root.
	 * 
	 * @param nombre
	 * @return
	 */
	Element crearElemento(String nombre) {
		return XmlHelper.getNuevoElemento(this.root, nombre);
	}

	/**
	 * Crea un atributo utilizando el doc del elemento root.
	 * 
	 * @param nombre
	 * @return
	 */
	Attr crearAtributo(String nombre) {
		return XmlHelper.getNuevoAtributo(this.root, nombre);
	}

	Element agregarElemento(Element elemento, String nombre) {
		return this.agregarElemento(elemento, nombre, null);
	}

	Element agregarElemento(Element elemento, String nombre, String valor) {
		Element hijo = this.crearElemento(nombre);
		hijo.setTextContent(valor);
		elemento.appendChild(hijo);
		return hijo;
	}

	Attr agregarAtributo(Element elemento, String nombre, String valor) {
		Attr atributo = this.crearAtributo(nombre);
		atributo.setNodeValue(valor);
		elemento.setAttributeNode(atributo);
		return atributo;
	}
	
	/**
	 * Obtiene el valor del atributo id de un elemento.
	 * 
	 * @param elemento
	 * @return
	 */
	String obtenerId(Element elemento) {
		return elemento.getAttribute(Constants.ID_ATTR);
	}
}
