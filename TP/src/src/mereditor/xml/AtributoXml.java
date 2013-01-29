package mereditor.xml;

import java.util.HashSet;

import mereditor.control.AtributoControl;
import mereditor.modelo.Atributo;

import org.w3c.dom.Element;

public class AtributoXml extends AtributoControl implements Xmlizable {

	public AtributoXml() {
	}

	public AtributoXml(AtributoControl componente) {
		this.id = componente.getId();
		this.nombre = componente.getNombre();
		this.tipo = componente.getTipo();

		this.atributos = new HashSet<Atributo>(componente.getAtributos());

		this.cardinalidadMinima = componente.getCardinalidadMinima();
		this.cardinalidadMaxima = componente.getCardinalidadMaxima();

		this.original = componente.getOriginal();
		this.formula = componente.getFormula();

		this.figures = componente.getFiguras();
	}

	@Override
	public Element toXml(ParserXML parser_) throws Exception {
		ModeloBaseParserXml parser=(ModeloBaseParserXml) parser_;
		Element elemento = parser.crearElemento(Constants.ATRIBUTO_TAG);
		parser.agregarId(elemento, this.id.toString());
		parser.agregarTipo(elemento, this.tipo.toString());
		parser.agregarNombre(elemento, nombre);

		// Cardinalidad
		parser.agregarCardinalidad(elemento, this.cardinalidadMinima, this.cardinalidadMaxima);

		// Formula u original seg�n tipo
		switch (this.tipo) {
		case DERIVADO_CALCULO:
			parser.agregarFormula(elemento, this.formula);
			break;
		case DERIVADO_COPIA:
			if(this.original != null)
				parser.agregarOriginal(elemento, this.original.getId());
			break;
		}

		if (this.atributos.size() > 0) {
			Element atributosElement = parser.agregarElementoAtributos(elemento);
			for (Atributo atributo : this.atributos)
				atributosElement.appendChild(parser.convertirXmlizable(atributo).toXml(parser));
		}

		return elemento;
	}

	@Override
	public void fromXml(Element elemento,ParserXML parser_) throws Exception {
		ModeloBaseParserXml parser=(ModeloBaseParserXml) parser_;
		this.id = parser.obtenerId(elemento);
		this.nombre = parser.obtenerNombre(elemento);
		this.tipo = TipoAtributo.valueOf(parser.obtenerTipo(elemento));

		parser.registrar(this);

		// Atributos
		for (Atributo atributo : parser.obtenerAtributos(elemento)) {
			atributo.setPadre(this);
			this.atributos.add(atributo);
		}

		// Formula u original seg�n tipo
		switch (this.tipo) {
		case DERIVADO_CALCULO:
			this.formula = parser.obtenerFormulaAtributo(elemento);
			break;
		case DERIVADO_COPIA:
			this.original = parser.obtenerOriginalAtributo(elemento);
			break;
		}

		// Cardinalidad
		String[] cardinalidad = parser.obtenerCardinalidad(elemento);
		if (cardinalidad != null) {
			this.cardinalidadMinima = cardinalidad[0];
			this.cardinalidadMaxima = cardinalidad[1];
		}
	}
}
