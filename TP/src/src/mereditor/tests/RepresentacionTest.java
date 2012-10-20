package mereditor.tests;

import java.util.Map;

import junit.framework.TestCase;
import mereditor.representacion.PList;
import mereditor.xml.SaverLoaderXML;

public class RepresentacionTest extends TestCase {
	
	private static final String PATH_TEST = "xml/test/test.xml";
	private SaverLoaderXML parser;
	
	protected void setUp() throws Exception {
		super.setUp();
		this.parser = new SaverLoaderXML(PATH_TEST);
	}
	
	public void testEncontrarRepresentacionPorId() {
		Map<String, PList> rep = this.parser.obtenerRepresentaciones("_1");
		assertTrue(rep != null);
	}
	
	public void testEncontrarRepresentacionPorIdVerificarCantidad() {
		Map<String, PList> rep = this.parser.obtenerRepresentaciones("_1");
		assertEquals(rep.size(), 1);
	}
}
