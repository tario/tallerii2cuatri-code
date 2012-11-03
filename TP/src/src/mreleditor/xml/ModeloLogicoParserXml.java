package mreleditor.xml;

import org.w3c.dom.Document;

import mereditor.modelo.Diagrama;
import mereditor.modelo.Proyecto;
import mereditor.xml.ParserXML;

public class ModeloLogicoParserXml extends ParserXML {

	private Proyecto proyecto;

	public ModeloLogicoParserXml(Proyecto proyecto, String path) throws Exception {
		super();
		setRoot(path);
		this.proyecto = proyecto;
	}

	public ModeloLogicoParserXml(Proyecto proyecto) throws Exception {
		this.proyecto = proyecto;
	}
	@Override
	public Document generarXml() throws Exception {
		ConversorDERaLogico conversor=ConversorDERaLogico.getInstance();
		Diagrama der=proyecto.getDiagramaRaiz();
		return null;
	}

	@Override
	public Object parsearXml() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
