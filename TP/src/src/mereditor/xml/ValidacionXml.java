package mereditor.xml;

import mereditor.modelo.Validacion;

import org.w3c.dom.Element;

public class ValidacionXml extends Validacion implements Xmlizable {

	public ValidacionXml() {
	}

	public ValidacionXml(Validacion validacion) {
		this.estado = validacion.getEstado();
		this.observaciones = validacion.getObservaciones();
	}

	@Override
	public Element toXml(ParserXML parser_) throws Exception {
		ModeloBaseParserXml parser=(ModeloBaseParserXml) parser_;
		
		Element elemento = parser.crearElemento(Constants.VALIDACION_TAG);
		parser.agregarEstado(elemento, this.estado.toString());
		parser.agregarObservaciones(elemento, this.observaciones);

		return elemento;
	}

	@Override
	public void fromXml(Element elemento, ParserXML parser_) throws Exception {
		ModeloBaseParserXml parser=(ModeloBaseParserXml) parser_;
		
		this.estado = EstadoValidacion.valueOf(parser.obtenerEstado(elemento));
		this.observaciones = parser.obtenerObservaciones(elemento);
	}
}
