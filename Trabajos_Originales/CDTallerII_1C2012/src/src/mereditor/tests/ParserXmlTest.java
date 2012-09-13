package mereditor.tests;

import java.util.Iterator;

import junit.framework.TestCase;
import mereditor.modelo.Atributo;
import mereditor.modelo.Atributo.TipoAtributo;
import mereditor.modelo.Diagrama;
import mereditor.modelo.Entidad;
import mereditor.modelo.Entidad.TipoEntidad;
import mereditor.modelo.Jerarquia;
import mereditor.modelo.Proyecto;
import mereditor.modelo.Relacion;
import mereditor.modelo.Relacion.EntidadRelacion;
import mereditor.modelo.Validacion.EstadoValidacion;
import mereditor.modelo.base.Componente;
import mereditor.xml.ParserXml;

import org.w3c.dom.Document;

public class ParserXmlTest extends TestCase {

	private static final String PATH_TEST = "xml/test/test.xml";
	private ParserXml parser;

	protected void setUp() throws Exception {
		super.setUp();
		this.parser = new ParserXml(PATH_TEST);
	}

	public void testEncontrarEntidadPorId() throws Exception {
		Entidad entidad = (Entidad) this.parser.resolver("_1");
		assertTrue(entidad != null);
	}

	public void testEncontrarEntidadPorIdVerificarCantidadAtributos() throws Exception {
		Entidad entidad = (Entidad) this.parser.resolver("_1");
		assertEquals(entidad.getAtributos().size(), 4);
	}

	public void testEncontrarEntidadPorIdVerificarTipo() throws Exception {
		Entidad entidad = (Entidad) this.parser.resolver("_1");
		assertEquals(entidad.getTipo(), TipoEntidad.MAESTRA_COSA);
	}

	public void testEncontrarEntidadPorIdVerificarNombre() throws Exception {
		Entidad entidad = (Entidad) this.parser.resolver("_1");
		assertEquals(entidad.getNombre(), "Localidad");
	}

	public void testEncontrarAtributoPorId() throws Exception {
		Atributo atributo = (Atributo) this.parser.resolver("_2");
		assertTrue(atributo != null);
	}

	public void testEncontrarAtributoPorIdVerificarNombre() throws Exception {
		Atributo atributo = (Atributo) this.parser.resolver("_2");
		assertEquals(atributo.getNombre(), "fila");
	}

	public void testEncontrarAtributoPorIdVerificarTipo() throws Exception {
		Atributo atributo = (Atributo) this.parser.resolver("_2");
		assertEquals(atributo.getTipo(), TipoAtributo.CARACTERIZACION);
	}

	public void testEncontrarAtributoPorIdVerificarCardinalidad() throws Exception {
		Atributo atributo = (Atributo) this.parser.resolver("_14");
		assertEquals(atributo.getCardinalidadMinima(), "1");
		assertEquals(atributo.getCardinalidadMaxima(), "n");
	}

	public void testEncontrarAtributoCopiaPorIdVerificarOriginal() throws Exception {
		Atributo atributo = (Atributo) this.parser.resolver("_2a");
		Atributo original = (Atributo) this.parser.resolver("_3");
		assertEquals(atributo.getOriginal(), original);
	}

	public void testEncontrarAtributoCalculoPorIdVerificarFormula() throws Exception {
		Atributo atributo = (Atributo) this.parser.resolver("_2b");
		assertEquals(atributo.getFormula(), "1 + 1");
	}

	public void testEncontrarAtributoCompuestoPorIdVerificarHijos() throws Exception {
		Atributo atributo = (Atributo) this.parser.resolver("_32");
		assertEquals(atributo.getAtributos().size(), 5);
	}

	public void testEncontrarJerarquiaPorIdVerificarGenerica() throws Exception {
		Jerarquia jerarquia = (Jerarquia) this.parser.resolver("_50");
		Componente generica = this.parser.resolver("_1");
		assertEquals(jerarquia.getGenerica(), generica);
		assertEquals(jerarquia.getDerivadas().size(), 2);
	}

	public void testEncontrarRelacionPorIdVerificarNombre() throws Exception {
		Relacion relacion = (Relacion) this.parser.resolver("_24");
		assertEquals(relacion.getNombre(), "SL");
	}

	public void testEncontrarRelacionPorIdVerificarParticipantes() throws Exception {
		Relacion relacion = (Relacion) this.parser.resolver("_24");
		assertEquals(relacion.getParticipantes().size(), 2);
	}

	public void testEncontrarRelacionPorIdVerificarAtributos() throws Exception {
		Relacion relacion = (Relacion) this.parser.resolver("_30");
		assertEquals(relacion.getAtributos().size(), 1);
	}

	public void testEncontrarRelacionPorIdVerificarAtributoCompuesto() throws Exception {
		Relacion relacion = (Relacion) this.parser.resolver("_30");
		assertEquals(relacion.getAtributos().iterator().next().getAtributos().size(), 5);
	}

	public void testEncontrarRelacionPorIdVerificarRolParticipante() throws Exception {
		Relacion relacion = (Relacion) this.parser.resolver("_24");
		assertEquals(relacion.getParticipantes().size(), 2);
		EntidadRelacion participante = relacion.getParticipantes().iterator().next();
		assertEquals(participante.getRol(), "Bosss");
	}
	
	public void testEncontrarRelacionPorIdVerificarCardinalidades() throws Exception {
		Relacion relacion = (Relacion) this.parser.resolver("_24");
		assertEquals(relacion.getParticipantes().size(), 2);
		
		Iterator<EntidadRelacion> iterator = relacion.getParticipantes().iterator();
		
		EntidadRelacion participante = iterator.next();
		assertEquals(participante.getCardinalidadMinima(), "1");
		assertEquals(participante.getCardinalidadMaxima(), "n");
		
		participante = iterator.next();
		assertEquals(participante.getCardinalidadMinima(), "1");
		assertEquals(participante.getCardinalidadMaxima(), "1");
	}

	public void testEncontrarDiagramaPorId() throws Exception {
		Diagrama diagrama = (Diagrama) this.parser.resolver("_41");
		assertTrue(diagrama != null);
	}

	public void testEncontrarDiagramaPorIdVerificarComponentes() throws Exception {
		Diagrama diagrama = (Diagrama) this.parser.resolver("_41");
		assertEquals(diagrama.getComponentes().size(), 7);
	}

	public void testEncontrarDiagramaPorIdVerificarValidacion() throws Exception {
		Diagrama diagrama = (Diagrama) this.parser.resolver("_41");
		assertEquals(diagrama.getValidacion().getEstado(), EstadoValidacion.SIN_VALIDAR);
		assertEquals(diagrama.getValidacion().getObservaciones(), "Falta validar");
	}

	public void testCargarProyecto() throws Exception {
		Proyecto proyecto = this.parser.parsear();
		assertTrue(proyecto != null);
		assertTrue(proyecto.getComponentes().size() > 0);
	}
	
	public void testCargarProyectoConvertirXml() throws Exception {
		Proyecto proyecto = this.parser.parsear();
		assertTrue(proyecto != null);
		// Crear parser nuevo con proyecto parseado.
		this.parser = new ParserXml(proyecto);
		Document doc =  this.parser.generarXmlComponentes();
		assertTrue(doc != null);
	}

}
