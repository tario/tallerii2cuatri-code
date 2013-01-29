package mereditor.xml;

import mereditor.control.DiagramaControl;
import mereditor.modelo.Diagrama;
import mereditor.modelo.Proyecto;
import mereditor.modelo.base.Componente;
import mreleditor.modelo.DiagramaLogico;

import org.w3c.dom.Element;

public class DiagramaXml extends DiagramaControl implements Xmlizable {

	public DiagramaXml(Proyecto proyecto) {
		super(proyecto);
	}

	public DiagramaXml(Proyecto proyecto, DiagramaControl componente) {
		this(proyecto);
		this.id = componente.getId();
		this.nombre = componente.getNombre();

		this.componentes = componente.getComponentes();
		this.diagramas = componente.getDiagramas();
		
		this.validacion = componente.getValidacion();
	}

	@Override
	public Element toXml(ParserXML parser_) throws Exception {
		ModeloBaseParserXml parser=(ModeloBaseParserXml) parser_;
		Element elemento = parser.crearElemento(Constants.DIAGRAMA_TAG);
		parser.agregarId(elemento, this.id.toString());
		parser.agregarNombre(elemento, nombre);

		// Agregar las referencias a los componentes
		if (this.componentes.size() > 0) {
			Element componentesElement = parser.agregarComponentes(elemento);
			for (Componente componente : this.componentes) {
				if (!componente.es(DiagramaLogico.class))
					parser.agregarComponente(componentesElement, componente.getId());
			}
		}

		// Agregar los diagramas hijos
		if (this.diagramas.size() > 0) {
			Element diagramasElement = parser.agregarDiagramas(elemento);
			for (Diagrama diagrama : this.diagramas) {
				diagramasElement.appendChild(parser.convertirXmlizable(diagrama).toXml(parser));
			}
		}

		// Agregar el resultado de la validacion
		elemento.appendChild(parser.convertirXmlizable(this.validacion).toXml(parser));

		return elemento;
	}

	@Override
	public void fromXml(Element elemento, ParserXML parser_) throws Exception {
		ModeloBaseParserXml parser=(ModeloBaseParserXml) parser_;
		
		this.id = elemento.getAttribute(Constants.ID_ATTR);
		this.nombre = XmlHelper.querySingle(elemento, Constants.NOMBRE_TAG).getTextContent();

		parser.registrar(this);

		// Componentes
		for (Componente componente : parser.obtenerComponentes(elemento)) {
			componente.setPadre(this);
			this.componentes.add(componente);
		}

		// Diagramas
		for (Componente componente : parser.obtenerDiagramas(elemento)) {
			componente.setPadre(this);
			this.diagramas.add((Diagrama) componente);
		}

		this.validacion = parser.obtenerValidacion(elemento);
	}
}
