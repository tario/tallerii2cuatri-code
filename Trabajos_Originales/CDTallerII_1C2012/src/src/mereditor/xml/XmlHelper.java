package mereditor.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlHelper {
	protected static XPathFactory xpath = XPathFactory.newInstance();

	/**
	 * Ejecuta una XQuery y devuelve el resultado en una lista de elementos
	 * 
	 * @param element
	 * @param query
	 * @return
	 */
	public static List<Element> query(Element element, String query) {
		try {
			XPathExpression expr = xpath.newXPath().compile(query);
			NodeList list = (NodeList) expr.evaluate(element, XPathConstants.NODESET);

			return XmlHelper.toElementList(list);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return new ArrayList<Element>();
	}

	public static Element querySingle(Element element, String query) {
		try {
			XPathExpression expr = xpath.newXPath().compile(query);
			NodeList list = (NodeList) expr.evaluate(element, XPathConstants.NODESET);

			if (list.getLength() > 0)
				return (Element) list.item(0);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static List<Element> toElementList(NodeList list) {
		ArrayList<Element> nodes = new ArrayList<>();
		for (int i = 0; i < list.getLength(); i++) {
			Node nodo = list.item(i);
			if (nodo instanceof Element)
				nodes.add((Element) nodo);
		}
		return nodes;
	}

	/**
	 * Devuelve una lista con los nombre de los atributos del elemento
	 * 
	 * @param elemento
	 * @return
	 */
	public static List<String> attributeNames(Element elemento) {
		List<String> nombres = new ArrayList<>();
		NamedNodeMap list = elemento.getAttributes();
		for (int i = 0; i < list.getLength(); i++) {
			nombres.add(list.item(i).getNodeName());
		}
		return nombres;
	}

	/**
	 * Genera un nuevo elemento utilizando el documento del elemento
	 * proporcionado.
	 * 
	 * @param elemento
	 * @param nombre
	 * @return
	 */
	public static Element getNuevoElemento(Element elemento, String nombre) {
		return elemento.getOwnerDocument().createElement(nombre);
	}

	/**
	 * Genera un nuevo atributo utilizando el documento del elemento
	 * proporcionado.
	 * 
	 * @param elemento
	 * @param nombre
	 * @return
	 */
	public static Attr getNuevoAtributo(Element elemento, String nombre) {
		return elemento.getOwnerDocument().createAttribute(nombre);
	}

}
