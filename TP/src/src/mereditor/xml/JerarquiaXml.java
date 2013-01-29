package mereditor.xml;

import mereditor.control.JerarquiaControl;
import mereditor.modelo.Entidad;
import mereditor.modelo.base.Componente;

import org.w3c.dom.Element;

public class JerarquiaXml extends JerarquiaControl implements Xmlizable {

	public JerarquiaXml() {
	}

	public JerarquiaXml(JerarquiaControl componente) {
		this.id = componente.getId();
		this.tipo = componente.getTipo();
		this.generica = componente.getGenerica();
		this.derivadas = componente.getDerivadas();
	}

	@Override
	public Element toXml(ParserXML parser_) throws Exception {
		ModeloBaseParserXml parser=(ModeloBaseParserXml) parser_;
		
		Element elemento = parser.crearElemento(Constants.JERARQUIA_TAG);
		parser.agregarId(elemento, this.id.toString());
		parser.agregarTipo(elemento, this.tipo.toString());

		parser.agregarGenerica(elemento, this.generica.getId());

		Element derivadasElement = parser.agregarDerivadas(elemento);
		for (Componente componente : this.derivadas)
			parser.agregarDerivada(derivadasElement, componente.getId());

		return elemento;
	}

	@Override
	public void fromXml(Element elemento, ParserXML parser_) throws Exception {
		ModeloBaseParserXml parser=(ModeloBaseParserXml) parser_;
		
		this.id = parser.obtenerId(elemento);
		this.tipo = TipoJerarquia.valueOf(parser.obtenerTipo(elemento));

		parser.registrar(this);

		this.generica = (Entidad) parser.obtenerGenerica(elemento);

		for (Componente componente : parser.obtenerDerivadas(elemento)) {
			this.derivadas.add((Entidad) componente);
		}
	}
}
