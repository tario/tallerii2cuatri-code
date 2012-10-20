package mereditor.xml;

import java.io.File;


import mereditor.modelo.Proyecto;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProyectoParserXml extends ParserXML {

	private String modeloPath;
	private String representacionPath;
	private Proyecto proyecto;

	
	public ProyectoParserXml(Proyecto proyecto)throws Exception{
		super();
		this.proyecto=proyecto;
	}
	public ProyectoParserXml(String path)throws Exception {
		setRoot(path);
		if (!this.validarFormato(this.root))
		throw new RuntimeException("Formato inválido del archivo del proyecto.");

	String dir = new File(path).getParent() + File.separator;

	modeloPath = dir + XmlHelper.querySingle(this.root, "./Modelo").getTextContent();
	representacionPath = dir + XmlHelper.querySingle(this.root, "./Representacion")
			.getTextContent();
	}
	
	String getModeloPath(){
		return modeloPath;
	}
	String getRepresentacionPath(){
		return representacionPath;
	}

	
	private boolean validarFormato(Element root) {
		if (XmlHelper.querySingle(root, "./Modelo") != null
				&& XmlHelper.querySingle(root, "./Representacion") != null)
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
		modelo.setTextContent(this.proyecto.getComponentesPath());
		representacion.setTextContent(this.proyecto.getRepresentacionPath());

		this.root.appendChild(modelo);
		this.root.appendChild(representacion);

		return doc;
	}

	@Override
	public Object parsearXml() {
		// TODO Auto-generated method stub
		return null;
	}



	
	
}
