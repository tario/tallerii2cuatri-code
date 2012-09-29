package mereditor.xml;

import java.io.File;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import mereditor.modelo.Proyecto;
import mereditor.modelo.base.Componente;
import mereditor.representacion.PList;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ParserXml {

	protected Proyecto proyecto;

	protected DocumentBuilder docBuilder;

	protected Element root;
	private ModeloParserXml modeloParser;
	private RepresentacionParserXml representacionParser;

	public ParserXml() throws Exception {
		this.docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}

	public ParserXml(Proyecto proyecto) throws Exception {
		this();
		this.proyecto = proyecto;
		this.modeloParser = new ModeloParserXml(this.proyecto);
		this.representacionParser = new RepresentacionParserXml(this.proyecto);
	}

	public ParserXml(String path) throws Exception {
		this();
		File source = new File(path);
		this.root = docBuilder.parse(source).getDocumentElement();
		this.proyecto = new Proyecto();
		this.proyecto.setPath(path);
		this.init(path);
	}

	/**
	 * Lee el archivo del proyecto y carga los archivos del modelo y la
	 * representación.
	 * 
	 * @param path
	 *            Ubicación del archivo del proyecto.
	 * @throws Exception
	 */
	private void init(String path) throws Exception {
		if (!this.validarFormato(this.root))
			throw new RuntimeException("Formato inválido del archivo del proyecto.");

		String dir = new File(path).getParent() + File.separator;

		String modeloPath = XmlHelper.querySingle(this.root, "./Modelo").getTextContent();
		String representacionPath = XmlHelper.querySingle(this.root, "./Representacion")
				.getTextContent();

		this.modeloParser = new ModeloParserXml(this.proyecto, dir + modeloPath);
		this.representacionParser = new RepresentacionParserXml(this.proyecto, dir
				+ representacionPath);
	}

	/**
	 * Verifica que el arhcivo xml tenga los elementos correspondientes a un
	 * archivo de proyecto.
	 * 
	 * @param root
	 *            Elemento raíz del documento xml del proyecto.
	 * @return <code>true</code> si el formato es válido, <code>false</code> de
	 *         otro forma.
	 */
	private boolean validarFormato(Element root) {
		if (XmlHelper.querySingle(root, "./Modelo") != null
				&& XmlHelper.querySingle(root, "./Representacion") != null)
			return true;

		return false;
	}

	/**
	 * Parsea los componentes y representaciones de un archivo de XML de un
	 * proyecto.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Proyecto parsear() throws Exception {
		this.modeloParser.parsearModelo();
		this.representacionParser.parsearRepresentacion();
		return this.proyecto;
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

	/**
	 * Devuelve el componente con el id asociado. Si no se encuentra registrado
	 * en la tabla de componentes, lo busca en el XML del modelo y lo parsea.
	 * 
	 * @param id
	 * @return Componente parseado
	 * @throws Exception
	 */
	public Componente resolver(String id) throws Exception {
		return this.modeloParser.resolver(id);
	}

	/**
	 * Generar el documento XML de componentes.
	 * 
	 * @return
	 * @throws DOMException
	 * @throws Exception
	 */
	public Document generarXmlProyecto() throws DOMException, Exception {
		Document doc = this.docBuilder.newDocument();
		this.root = doc.createElement(Constants.PROYECTO_TAG);
		doc.appendChild(this.root);

		Element modelo = this.crearElemento(Constants.MODELO_TAG);
		Element representacion = this.crearElemento(Constants.REPRESENTACION_TAG);
		modelo.setTextContent(this.proyecto.getComponentesPath());
		representacion.setTextContent(this.proyecto.getRepresentacionPath());

		this.root.appendChild(modelo);
		this.root.appendChild(representacion);

		return doc;
	}

	/**
	 * Generar el documento XML de componentes.
	 * 
	 * @return
	 * @throws DOMException
	 * @throws Exception
	 */
	public Document generarXmlComponentes() throws DOMException, Exception {
		return this.modeloParser.generarXml();
	}

	/**
	 * Generar el documento XML de representacion.
	 * 
	 * @return
	 * @throws DOMException
	 * @throws Exception
	 */
	public Document generarXmlRepresentacion() throws DOMException, Exception {
		return this.representacionParser.generarXml();
	}

	/**
	 * Método que se hace disponible para fines de testing. Encuentra y devuelve
	 * las representaciones de un componente para cada diagrama en el que este
	 * presente en el archivo.
	 * 
	 * @param string
	 * @return
	 */
	public Map<String, PList> obtenerRepresentaciones(String string) {
		return this.representacionParser.obtenerRepresentaciones(string);
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

}
