package mereditor.xml;

import java.util.Map;

import mereditor.modelo.Proyecto;
import mereditor.modelo.base.Componente;
import mereditor.representacion.PList;
import mreleditor.xml.ModeloLogicoParserXml;
import mreleditor.xml.ParserRepresentacionDER;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

public class SaverLoaderXML {

	protected Proyecto proyecto;

	private ProyectoParserXml proyectoParser;
	private ModeloBaseParserXml modeloParser;
	private ModeloLogicoParserXml modeloLogicoParser;
	private RepresentacionParserXml representacionParser;
	private ParserRepresentacionDER representacionParserDER;

	public SaverLoaderXML() {
	}

	public SaverLoaderXML(Proyecto proyecto) throws Exception {
		this.proyecto = proyecto;
		this.proyectoParser = new ProyectoParserXml(this.proyecto);
		this.modeloParser = new ModeloBaseParserXml(this.proyecto);
		this.modeloLogicoParser = new ModeloLogicoParserXml(this.proyecto);
		this.representacionParser = new RepresentacionParserXml(this.proyecto);
		this.representacionParserDER = new ParserRepresentacionDER(this.proyecto);
	}

	public SaverLoaderXML(String path) throws Exception {
		this.proyecto = new Proyecto();
		this.proyecto.setPath(path);
		this.proyectoParser = new ProyectoParserXml(path);
		this.modeloParser = new ModeloBaseParserXml(proyecto, proyectoParser.getModeloPath());

		if (proyectoParser.hasLogic()) {
			this.modeloLogicoParser = new ModeloLogicoParserXml(proyecto, proyectoParser.getModeloLogicoPath());

			this.representacionParserDER = new ParserRepresentacionDER(proyecto,
					proyectoParser.getRepresentacionDERPath());
		}
		this.representacionParser = new RepresentacionParserXml(proyecto, proyectoParser.getRepresentacionPath());
	}

	/**
	 * Parsea los componentes y representaciones de un archivo de XML de un
	 * proyecto.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Proyecto load() throws Exception {
		this.modeloParser.parsearXml();
		this.representacionParser.parsearXml();

		if (proyectoParser.hasLogic()) {
			this.modeloLogicoParser.parsearXml();
			this.representacionParserDER.parsearXml();
		}
		return this.proyecto;
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
		try {
			return this.modeloParser.resolver(id);
		} catch (Exception e) {
			return this.modeloLogicoParser.resolver(id);
		}

	}

	/**
	 * Generar el documento XML de componentes.
	 * 
	 * @return
	 * @throws DOMException
	 * @throws Exception
	 */
	public Document saveProyecto() throws DOMException, Exception {
		return proyectoParser.generarXml();
	}

	/**
	 * Generar el documento XML de componentes.
	 * 
	 * @return
	 * @throws DOMException
	 * @throws Exception
	 */
	public Document saveComponentes() throws DOMException, Exception {
		return this.modeloParser.generarXml();
	}

	public Document saveComponentesLogicos() throws DOMException, Exception {
		return this.modeloLogicoParser.generarXml();
	}

	/**
	 * Generar el documento XML de representacion.
	 * 
	 * @return
	 * @throws DOMException
	 * @throws Exception
	 */
	public Document saveRepresentacion() throws DOMException, Exception {
		return this.representacionParser.generarXml();
	}

	/**
	 * Generar el documento XML de representacion DER.
	 * 
	 * @return
	 * @throws DOMException
	 * @throws Exception
	 */
	public Document saveRepresentacionDER() throws DOMException, Exception {
		return this.representacionParserDER.generarXml();
	}

	/**
	 * Método que se hace disponible para fines de testing. Encuentra y
	 * devuelve las representaciones de un componente para cada diagrama en el
	 * que este presente en el archivo.
	 * 
	 * @param string
	 * @return
	 */
	public Map<String, PList> obtenerRepresentaciones(String string) {
		return this.representacionParser.obtenerRepresentaciones(string);
	}

	/**
	 * Método que se hace disponible para fines de testing. Encuentra y
	 * devuelve las representaciones de un componente para cada diagrama en el
	 * que este presente en el archivo.
	 * 
	 * @param string
	 * @return
	 */
	public Map<String, PList> obtenerRepresentacionesDER(String string) {
		return this.representacionParserDER.obtenerRepresentaciones(string);
	}
}
