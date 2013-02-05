package mreleditor.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


import mereditor.control.DiagramaLogicoControl;
import mereditor.control.TablaControl;
import mereditor.modelo.Proyecto;
import mereditor.modelo.base.Componente;
import mereditor.xml.Constants;
import mereditor.xml.ModeloParserXml;
import mereditor.xml.XmlHelper;
import mereditor.xml.Xmlizable;
import mreleditor.modelo.DiagramaLogico;
import mreleditor.modelo.Tabla;
import mreleditor.modelo.Tabla.ClaveForanea;


public class ModeloLogicoParserXml extends ModeloParserXml {


	public ModeloLogicoParserXml(Proyecto proyecto, String path) throws Exception {
		super(proyecto,path);
	}

	public ModeloLogicoParserXml(Proyecto proyecto) throws Exception {
		super(proyecto);
	}

	@Override
	public Document generarXml() throws Exception {
		Document doc = this.docBuilder.newDocument();
		this.root = doc.createElement(Constants.PROYECTO_TAG);
		doc.appendChild(this.root);

		for (Componente componente : proyecto.getComponentes()) {
			if (componente.es(DiagramaLogico.class)) {
				this.root.appendChild(this.convertirXmlizable(componente).toXml(this));
			}
		}
		
		for (Componente componente : proyecto.getComponentes()) {
			if (componente.es(DiagramaLogico.class)) {
				DiagramaLogicoControl log = (DiagramaLogicoControl) componente;
				
				ArrayList<Tabla> tablas = log.getTablas();
				Iterator<Tabla> it = tablas.iterator();
				
				while(it.hasNext()) {
					TablaControl tabControl = (TablaControl)(it.next());
					this.root.appendChild(this.convertirXmlizable(tabControl).toXml(this));
				}
				
			}
				
		}

		return doc;
	}

	protected Xmlizable convertirXmlizable(Object componente) throws Exception {
		if (Tabla.class.isInstance(componente))
			return new TablaXml((TablaControl) componente);
		if (DiagramaLogico.class.isInstance(componente))
			return new DiagramaLogicoXml(this.proyecto,(DiagramaLogicoControl) componente);

		throw new Exception("No existe un mapeo para: " + componente.toString());
	}

	@Override
	public Object parsearXml() throws Exception {
		// Obtener el id del diagrama principal
		Element diagramaXml = XmlHelper.querySingle(this.root, Constants.DIAGRAMA_LOGICO_QUERY);
		if (diagramaXml != null) {
			DiagramaLogico diagrama = (DiagramaLogico) this.resolver(this.obtenerId(diagramaXml));
			this.proyecto.setDiagramaLogico(new DiagramaLogicoControl(diagrama));
		}
		/*
		 * Recorrer todos los elemento de primer nivel (menos validacion) para
		 * tener en cuenta los que existen pero no fueron agregados a ning�n
		 * diagrama.
		 */
		List<Element> elementos = XmlHelper.query(this.root, Constants.ELEMENTOS_PRIMER_NIVEL_QUERY);
		for (Element elemento : elementos)
			this.resolver(this.obtenerId(elemento));
		return proyecto;
	}

	/**
	 * Devuelve una instancia de la clase correspondiente de parseo seg�n el
	 * nombre del elemento a parsear.
	 * 
	 * @param element
	 * @return
	 * @throws Exception
	 */
	protected Xmlizable mapeoXmlizable(Element element) throws Exception {
		switch (element.getNodeName()) {
		case Constants.DIAGRAMA_LOGICO_TAG:
			return new DiagramaLogicoXml(this.proyecto);
		case Constants.TABLA_TAG:
			return new TablaXml();
		
		}

		throw new Exception("No existe un mapeo para: " + element.getNodeName());
	}

	public Element agregarTablas(Element elemento) {
		return this.agregarElemento(elemento, Constants.TABLAS_TAG);
	}

	public Element agregarTabla(Element tablasElement, String id) {
		Element tablaElement = this.agregarElemento(tablasElement, Constants.TABLA_TAG);
		this.agregarAtributo(tablaElement, Constants.IDREF_ATTR, id);
		return tablaElement;
		
	}

	public List<Tabla> obtenerTablas(Element elemento) throws Exception {
		List<Element> tablasXml = XmlHelper.query(elemento, Constants.DIAGRAMA_TABLAS_QUERY);
		List<Tabla> tablas = new ArrayList<>();

		for (Element tablaXml : tablasXml)
			tablas.add((Tabla)this.obtenerReferencia(tablaXml));

		return tablas;
	}

	public Element agregarClavePrimaria(Element elemento) {
		return this.agregarElemento(elemento, Constants.CLAVE_PRIMARIA_TAG, "");
	}

	public Element agregarAtributoClavePrimaria(Element pkElement, String atributo) {
		return this.agregarElemento(pkElement, Constants.ATRIBUTO_TAG, atributo);
		
	}

	public Element agregarAtributos(Element elemento) {
		return this.agregarElemento(elemento, Constants.ATRIBUTOS_TAG, "");
	}

	public Element agregarAtributo(Element atributosXml, String atributo) {
		return this.agregarElemento(atributosXml, Constants.ATRIBUTO_TAG, atributo);
		
	}

	public Element agregarClaveForanea(Element elemento) {
		return this.agregarElemento(elemento, Constants.CLAVE_FORANEA_TAG, "");
	}

	public Element agregarTablaReferenciada(Element claveForaneaXml, String tablaReferenciada) {
		return this.agregarElemento(claveForaneaXml, Constants.TABLA_REFERENCIADA_TAG, tablaReferenciada);
		
	}

	public List<String> obtenerAtributos(Element elemento) {
		List<Element> atributosXml = XmlHelper.query(elemento, Constants.ATRIBUTOS_QUERY);
		List<String> atributos = new ArrayList<>();

		for (Element atributoXml : atributosXml) {
			atributos.add(atributoXml.getTextContent());
		}

		return atributos;
	}

	public List<String> obtenerClavePrimaria(Element elemento) {
		List<Element> primariasXml = XmlHelper.query(elemento, Constants.CLAVE_PRIMARIA_QUERY);
		List<String> primarias = new ArrayList<>();

		for (Element primariaXml : primariasXml) {
			primarias.add(primariaXml.getTextContent().trim());
		}

		return primarias;

	}

	public List<ClaveForanea> obtenerClaveForanea(Element elemento,Tabla tabla) {
		List<Element> foraneasXml = XmlHelper.query(elemento, Constants.FORANEAS_QUERY);
		List<ClaveForanea> foraneas = new ArrayList<>();

		for (Element foraneaXml : foraneasXml) {
			String referenciada=XmlHelper.querySingle(foraneaXml, Constants.TABLA_REFERENCIADA_QUERY).getTextContent();
			List<String> atributos=new ArrayList<>();
			for(Element atributo: XmlHelper.query(foraneaXml,Constants.ATRIBUTOS_QUERY)){
				atributos.add(atributo.getTextContent());
			}
			foraneas.add(tabla.new ClaveForanea(atributos, referenciada));
			
		}

		return foraneas;
	}

}
