package mreleditor.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mereditor.control.Control;
import mereditor.control.DiagramaLogicoControl;
import mereditor.interfaz.swt.figuras.Figura;
import mereditor.interfaz.swt.figuras.TablaFigure;
import mereditor.modelo.Proyecto;
import mereditor.modelo.base.Componente;
import mereditor.representacion.PList;
import mereditor.xml.Constants;
import mereditor.xml.ParserXML;
import mereditor.xml.XmlHelper;
import mreleditor.modelo.DiagramaLogico;

public class ParserRepresentacionDER extends ParserXML {

	private Proyecto proyecto;

	/**
	 * Constructor utilizado para leer el xml del path especificado
	 * 
	 * @param proyecto
	 *            Instancia vacia del proyecto.
	 * @param path
	 *            Ubicacion del archivo XML de representacion.
	 * @throws Exception
	 */
	public ParserRepresentacionDER(Proyecto proyecto, String path)
			throws Exception {
		super();
		setRoot(path);
		this.proyecto = proyecto;
		if (proyecto.getComponentes().size() > 1)
			throw new Exception(
					"El proyecto debe contener solo el diagrama raiz.");
	}

	/**
	 * Constructor a utilizar cuando se quiere generar el XML de representacion
	 * en base el objeto proyecto.
	 * 
	 * @param proyecto
	 * @throws Exception
	 */
	public ParserRepresentacionDER(Proyecto proyecto) throws Exception {
		this.proyecto = proyecto;
	}

	/**
	 * Generar el XML de rerpesentacion.
	 * @return
	 */
	@Override
	public Document generarXml() throws Exception {
		Document doc = this.docBuilder.newDocument();
		this.root = doc.createElement(Constants.PROYECTO_TAG);
		doc.appendChild(this.root);

		for(Componente componente: proyecto.getComponentes())
			if(DiagramaLogico.class.isInstance(componente))
				this.generarDiagramaXml(this.root, (DiagramaLogico)componente);

		return doc;
	}

	/**
	 * Recorre la coleccion de componentes del proyecto y busca sus
	 * representaciones para cada diagrama en el que estan presentes.
	 */
	@Override
	public Object parsearXml() throws Exception {
		// Para cada componente del proyecto
		for (Componente componente : this.proyecto.getComponentes()) {
			// Buscar las representaciones en cada diagrama
			Map<String, PList> representaciones = this
					.obtenerRepresentaciones(componente.getId());
			// Asignarselas a las figura correspondiente de cada diagrama
			Control<?> control = (Control<?>) componente;
			for (String idDiagrama : representaciones.keySet()){
				control.getFigura(idDiagrama).setRepresentacion(
						representaciones.get(idDiagrama));
				
			}
		}
		return proyecto;
	}

	/**
	 * Parsea un elemento de representacion basico con posicion y dimension.
	 * 
	 * @param elemento
	 * @return
	 */
	protected PList obtenerRepresentacion(Element elemento) {
		PList representacion = new PList();
		for (Element elementoHijo : XmlHelper.query(elemento, "./*")) {
			// Si es una lista de representaciones
			PList hijo = this.obtenerRepresentacion(elementoHijo);
			representacion.set(elementoHijo.getNodeName(), hijo);
		}
		for (String nombre : XmlHelper.attributeNames(elemento)) {
			representacion.set(nombre, elemento.getAttribute(nombre));
		}
		return representacion;
	}
	
	/**
	 * Devuelve un mapa con las representaciones del componente por cada
	 * diagrama en el que se encuentra.
	 * 
	 * @param id
	 *            Id del componente
	 * @return Mapa con los ids de los diagramas como clave y las
	 *         representaciones como valor.
	 */
	public Map<String, PList> obtenerRepresentaciones(String id) {
		HashMap<String, PList> representaciones = new HashMap<>();

		// Buscar todas las representaciones para el id
		String query = String.format(Constants.REPRESENTACION_LOGICA_ID_QUERY, id);
		List<Element> representacionesXml = XmlHelper.query(this.root, query);

		for (Element representacionXml : representacionesXml) {
			Element diagramaXml = XmlHelper.querySingle(representacionXml, Constants.DIAGRAMA_LOGICO_PADRE_QUERY);

			String idDiagrama = this.obtenerId(diagramaXml);
			representaciones.put(idDiagrama, this.obtenerRepresentacion(representacionXml));
		}

		return representaciones;
	}
	
	/**
	 * Genera el element de XML de representacion de un diagrama y sus hijos.
	 * Agrega el elemento generado al nodo raiz.
	 * 
	 * @param elemento
	 * @param diagrama
	 */
	protected void generarDiagramaXml(Element elemento, DiagramaLogico diagrama) {
		Element diagramaElem = this.agregarElemento(elemento, Constants.DIAGRAMA_LOGICO_TAG);
		this.agregarAtributo(diagramaElem, Constants.ID_ATTR, diagrama.getId());

		this.generarDiagramaXml(diagramaElem, diagrama.getId(), ((DiagramaLogicoControl)diagrama).getListaObjetosLogicos());

		// Recorrer todos los diagramas hijos del principal
		/*for (Diagrama diagramaHijo : diagrama.getDiagramas()) {
			this.generarDiagramaXml(elemento, diagramaHijo);
		}*/
	}
	
	@SuppressWarnings("rawtypes")
	protected void generarDiagramaXml(Element diagramaElem, String idDiagrama, ArrayList<Figura> componentes) {
		// Recorrer todos los componentes y sus hijos.
		if(componentes != null) {
			for (Figura figura : componentes) {
				//Control<?> control = (Control<?>) componente;
				//Figura<?> figura = control.getFigura(idDiagrama);
	
				if (figura != null && figura.getRepresentacion() != null) {
					PList plist = figura.getRepresentacion();
					Element reprElement = this.agregarElemento(diagramaElem, Constants.REPRESENTACION_LOGICA_TAG);
					this.agregarAtributo(reprElement, Constants.ID_ATTR, ((TablaFigure)figura).getID());
					this.agregarRepresentacion(reprElement, plist);
				}
				
				//this.generarDiagramaXml(diagramaElem, idDiagrama, componente.getComponentes());
			}
		}
	}

	/**
	 * Genera un elemento Representacion XML desde una PList.
	 * 
	 * @param elemento
	 * @param repr
	 */
	private void agregarRepresentacion(Element elemento, PList repr) {
		for (String nombre : repr.getNames()) {
			Object valor = repr.get(nombre);

			if (valor instanceof PList) {
				// Si el valor es una PList entonces armar un elemento
				Element elemHijo = this.agregarElemento(elemento, nombre);
				this.agregarRepresentacion(elemHijo, (PList) valor);
			} else {
				// Si no agregar como par atributo/valor
				this.agregarAtributo(elemento, nombre, valor.toString());
			}
		}
	}
	
}
