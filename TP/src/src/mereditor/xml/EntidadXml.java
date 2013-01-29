package mereditor.xml;

import java.util.HashSet;

import mereditor.control.EntidadControl;
import mereditor.modelo.Atributo;
import mereditor.modelo.Entidad;

import org.w3c.dom.Element;

public class EntidadXml extends EntidadControl implements Xmlizable {

	public EntidadXml() {}

	public EntidadXml(EntidadControl componente) {
		this.id = componente.getId();
		this.nombre = componente.getNombre();
		this.tipo = componente.getTipo();

		this.atributos = new HashSet<Atributo>(componente.getAtributos());
		this.identificadores = componente.getIdentificadores();
		
		this.figures = componente.getFiguras();
	}

	@Override
	public Element toXml(ParserXML parser_) throws Exception {
		ModeloBaseParserXml parser=(ModeloBaseParserXml) parser_;
		
		Element elemento = parser.crearElemento(Constants.ENTIDAD_TAG);
		parser.agregarId(elemento, this.id.toString());
		parser.agregarTipo(elemento, this.tipo.toString());
		parser.agregarNombre(elemento, nombre);

		if (this.atributos.size() > 0) {
			Element atributosElement = parser.agregarElementoAtributos(elemento);
			for (Atributo atributo : this.atributos)
				atributosElement.appendChild(parser.convertirXmlizable(atributo).toXml(parser));
		}

		if (this.identificadores.size() > 0) {
			Element identificadoresXml = parser.agregarIdentificadores(elemento);

			for (Identificador identificador : this.identificadores) {
				Element identificadorXml = parser.agregarIdentificador(identificadoresXml);
				
				for(Atributo attr : identificador.getAtributos())
					parser.agregarReferenciaAtributo(identificadorXml, attr.getId());
				
				for(Entidad entidad : identificador.getEntidades())
					parser.agregarReferenciaEntidad(identificadorXml, entidad.getId());
			}
		}

		return elemento;
	}

	@Override
	public void fromXml(Element elemento, ParserXML parser_) throws Exception {
		ModeloBaseParserXml parser=(ModeloBaseParserXml) parser_;
		
		this.id = parser.obtenerId(elemento);
		this.nombre = parser.obtenerNombre(elemento);
		this.tipo = TipoEntidad.valueOf(parser.obtenerTipo(elemento));

		parser.registrar(this);

		for (Atributo atributo : parser.obtenerAtributos(elemento)) {
			atributo.setPadre(this);
			this.atributos.add(atributo);
		}

		// Obtener identificadores externos
		this.identificadores.addAll(parser.obtenerIdentificadores(elemento, this));
	}
}
