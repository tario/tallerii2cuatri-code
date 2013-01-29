package mereditor.xml;

import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import mereditor.modelo.Proyecto;
import mereditor.modelo.base.Componente;

public abstract class ModeloParserXml extends ParserXML {
	
	protected Proyecto proyecto;

	public ModeloParserXml(Proyecto proyecto, String path) throws Exception {
		super();
		setRoot(path);
		this.proyecto = proyecto;
	}
	public ModeloParserXml(Proyecto proyecto) throws Exception {
		this.proyecto = proyecto;
	}
	
	/**
	 * Devuelve el componente con el id asociado. Si no se encuentra registrado
	 * en la tabla de componentes, lo busca en el XML del modelo y lo parsea.
	 * 
	 * @param id
	 * @return Componente parseado
	 * @throws Exception
	 */
	public Componente resolver(String id) throws Exception {
		if (this.proyecto.contiene(id))
			return this.proyecto.getComponente(id);

		return this.buscarParsear(id);
	}
	
	/**
	 * Toma el id de referencia del elemento y lo trata de resolver.
	 * 
	 * @param elemento
	 * @return
	 * @throws Exception
	 */
	protected Componente obtenerReferencia(Element elemento) throws Exception {
		String id = elemento.getAttribute(Constants.IDREF_ATTR);
		return this.resolver(id);
	}

	/**
	 * Registra el componente en la tabla de componentes utilizando el id como
	 * clave.
	 * 
	 * @param componente
	 * @throws Exception
	 */
	public void registrar(Componente componente) throws Exception {
		if (componente.getId() == null)
			throw new Exception("No se puede agregar un componente sin identificador.");

		if (this.proyecto.contiene(componente.getId()))
			throw new Exception("Identificador duplicado: " + componente.getId());

		this.proyecto.agregar(componente);
	}
	/**
	 * Busca un elemento con el id especificado y trata de parsearlo según el
	 * tipo de elemento.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	protected Componente buscarParsear(String id) throws Exception {
		String query = String.format(Constants.ID_QUERY, id);
		List<Element> list = XmlHelper.query(this.root, query);

		if (list.size() == 1)
			return this.parsearComponente(list.get(0));

		throw new Exception("Identificador inexistente o duplicado: '" + id + "'");
	}

	/**
	 * Parsea un elemento según la implementacion de la instancia devuelta por
	 * mapElement.
	 * 
	 * @param element
	 * @return
	 * @throws Exception
	 */
	protected Componente parsearComponente(Element element) throws Exception {
		Xmlizable xmlizable = this.mapeoXmlizable(element);
		xmlizable.fromXml(element, this);
		return (Componente) xmlizable;
	}
	protected abstract Xmlizable mapeoXmlizable(Element element)throws Exception;
	
	public Element agregarNombre(Element elemento, String valor) {
		return this.agregarElemento(elemento, Constants.NOMBRE_TAG, valor);
	}
	/**
	 * Obtiene el valor contenido en el tag hijo "Nombre" de un elemento.
	 * 
	 * @param elemento
	 * @return
	 */
	public String obtenerNombre(Element elemento) {
		return XmlHelper.querySingle(elemento, Constants.NOMBRE_TAG).getTextContent();
	}
	public Attr agregarId(Element elemento, String valor) {
		return this.agregarAtributo(elemento, Constants.ID_ATTR, valor);
	}

}
