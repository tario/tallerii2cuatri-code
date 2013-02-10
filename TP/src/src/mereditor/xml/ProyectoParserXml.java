package mereditor.xml;

import java.io.File;

import mereditor.modelo.Proyecto;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProyectoParserXml extends ParserXML {

	private String modeloPath;
	private String modeloLogicoPath;
	private String representacionPath;
	private String representacionDERPath;
	private Proyecto proyecto;

	private boolean hasLogic = false;

	public ProyectoParserXml(Proyecto proyecto) throws Exception {
		super();
		this.proyecto = proyecto;
	}

	public ProyectoParserXml(String path) throws Exception {
		setRoot(path);
		if (!this.validarFormato(this.root))
			throw new RuntimeException("Formato inválido del archivo del proyecto.");

		String dir = new File(path).getParent() + File.separator;

		modeloPath = dir + XmlHelper.querySingle(this.root, "./Modelo").getTextContent();
		representacionPath = dir + XmlHelper.querySingle(this.root, "./Representacion").getTextContent();

		if (hasLogic) {
			representacionDERPath = dir + XmlHelper.querySingle(this.root, "./RepresentacionLogica").getTextContent();
			modeloLogicoPath = dir + XmlHelper.querySingle(this.root, "./DiagramaLogico").getTextContent();
		}
	}
	public boolean hasLogic(){
		return hasLogic;
	}
	String getModeloPath() {
		return modeloPath;
	}

	String getRepresentacionPath() {
		return representacionPath;
	}

	String getRepresentacionDERPath() {
		return representacionDERPath;
	}

	String getModeloLogicoPath() {
		return modeloLogicoPath;
	}

	private boolean validarFormato(Element root) {
		if (XmlHelper.querySingle(root, "./RepresentacionLogica") != null
				&& XmlHelper.querySingle(root, "./DiagramaLogico") != null)
			hasLogic = true;

		if (XmlHelper.querySingle(root, "./Modelo") != null && XmlHelper.querySingle(root, "./Representacion") != null

		)
			return true;

		return false;
	}

	@Override
	public Document generarXml() throws DOMException, Exception {
		Document doc = this.docBuilder.newDocument();
		this.root = doc.createElement(Constants.PROYECTO_TAG);
		doc.appendChild(this.root);

		Element modelo = this.crearElemento(Constants.MODELO_TAG);
		Element representacion = this.crearElemento(Constants.REPRESENTACION_TAG);
		Element representacionDER = this.crearElemento(Constants.REPRESENTACION_LOGICA_TAG);
		Element modeloLogico = this.crearElemento(Constants.DIAGRAMA_LOGICO_TAG);
		modelo.setTextContent(this.proyecto.getComponentesPath());
		representacion.setTextContent(this.proyecto.getRepresentacionPath());
		representacionDER.setTextContent(this.proyecto.getRepresentacionDERPath());
		modeloLogico.setTextContent(this.proyecto.getComponentesLogicosPath());

		this.root.appendChild(modelo);
		this.root.appendChild(representacion);
		this.root.appendChild(representacionDER);
		this.root.appendChild(modeloLogico);

		return doc;
	}

	@Override
	public Object parsearXml() {
		// TODO Auto-generated method stub
		return null;
	}

}
