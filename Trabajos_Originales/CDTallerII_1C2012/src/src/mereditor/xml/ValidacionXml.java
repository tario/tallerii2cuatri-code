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
	public Element toXml(ModeloParserXml parser) throws Exception {
		Element elemento = parser.crearElemento(Constants.VALIDACION_TAG);
		parser.agregarEstado(elemento, this.estado.toString());
		parser.agregarObservaciones(elemento, this.observaciones);

		return elemento;
	}

	@Override
	public void fromXml(Element elemento, ModeloParserXml parser) throws Exception {
		this.estado = EstadoValidacion.valueOf(parser.obtenerEstado(elemento));
		this.observaciones = parser.obtenerObservaciones(elemento);
	}
}
