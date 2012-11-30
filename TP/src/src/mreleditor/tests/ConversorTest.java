package mreleditor.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import mereditor.modelo.Atributo;
import mereditor.modelo.Diagrama;
import mereditor.modelo.Entidad;
import mereditor.modelo.Atributo.TipoAtributo;
import mereditor.modelo.Entidad.TipoEntidad;
import mreleditor.conversor.ConversorDERaLogico;
import mreleditor.modelo.DiagramaLogico;
import mreleditor.modelo.Tabla;



public class ConversorTest {
	
	
	ConversorDERaLogico conversor;
	DiagramaLogico diagramaLogico;
	
	@Before
	public void init(){
		conversor= ConversorDERaLogico.getInstance();
		diagramaLogico=new DiagramaLogico();
	}
	
	@Test
	public void conversionDeUnaEntidadSimple(){
		Diagrama der=new Diagrama(null);
		Entidad auto= new Entidad("Auto");
		auto.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo patente= new Atributo("patente");
		patente.setTipo(TipoAtributo.CARACTERIZACION);
		patente.setCardinalidadMaxima("1");
		patente.setCardinalidadMinima("1");
		auto.addAtributo(patente);
		
		Entidad.Identificador idAuto= auto.new Identificador(auto);
		idAuto.addAtributo(patente);
		auto.addIdentificador(idAuto);
		
		Atributo color= new Atributo("color");
		color.setTipo(TipoAtributo.CARACTERIZACION);
		color.setCardinalidadMaxima("1");
		color.setCardinalidadMinima("1");
		auto.addAtributo(color);
		
		der.agregar(auto);
		
		diagramaLogico=conversor.convertir(der);
		
		List<Tabla> tablas=diagramaLogico.getTablas();
		
		Assert.assertTrue(tablas.size()==1);
		Tabla tablaAuto= tablas.get(0);
		Tabla expected=new Tabla("Auto");
		expected.addClavePrimaria("patente");
		expected.addAtributo("patente");
		expected.addAtributo("color");
		
		Assert.assertEquals(expected, tablaAuto);
		
	}
	@Test
	public void convertirEntidadConAtributoCompuesto(){
		Diagrama der=new Diagrama(null);
		Entidad auto= new Entidad("Auto");
		auto.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo patente= new Atributo("patente");
		patente.setTipo(TipoAtributo.CARACTERIZACION);
		patente.setCardinalidadMaxima("1");
		patente.setCardinalidadMinima("1");
		auto.addAtributo(patente);
		
		Entidad.Identificador idAuto= auto.new Identificador(auto);
		idAuto.addAtributo(patente);
		auto.addIdentificador(idAuto);
		Atributo duenio=new Atributo("duenio");
		duenio.setCardinalidadMaxima("1");
		duenio.setCardinalidadMinima("1");
		duenio.setTipo(TipoAtributo.CARACTERIZACION);
		Atributo nombreDuenio=new Atributo("nombre");
		duenio.setCardinalidadMaxima("1");
		duenio.setCardinalidadMinima("1");
		duenio.setTipo(TipoAtributo.CARACTERIZACION);
		Atributo dniDuenio=new Atributo("dni");
		duenio.setCardinalidadMaxima("1");
		duenio.setCardinalidadMinima("1");
		duenio.setTipo(TipoAtributo.CARACTERIZACION);
		
		duenio.addAtributo(nombreDuenio);
		duenio.addAtributo(dniDuenio);
		
		auto.addAtributo(duenio);
		
		der.agregar(auto);
		
		diagramaLogico=conversor.convertir(der);
		
		List<Tabla> tablas=diagramaLogico.getTablas();
		
		Assert.assertTrue(tablas.size()==1);
		Tabla tablaAuto= tablas.get(0);
		
		Tabla expected=new Tabla("Auto");
		expected.addClavePrimaria("patente");
		expected.addAtributo("patente");
		expected.addAtributo("duenio-nombre");
		expected.addAtributo("duenio-dni");
		
		Assert.assertEquals(expected, tablaAuto);

	}
	@Test
	public void convertirEntidadConAtributoCompuestoComoClave(){
		Diagrama der=new Diagrama(null);
		Entidad auto= new Entidad("Auto");
		auto.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo patente= new Atributo("patente");
		patente.setTipo(TipoAtributo.CARACTERIZACION);
		patente.setCardinalidadMaxima("1");
		patente.setCardinalidadMinima("1");
		Atributo letras=new Atributo("letras");
		letras.setTipo(TipoAtributo.CARACTERIZACION);
		letras.setCardinalidadMaxima("1");
		letras.setCardinalidadMinima("1");
		Atributo numeros=new Atributo("numeros");
		numeros.setTipo(TipoAtributo.CARACTERIZACION);
		numeros.setCardinalidadMaxima("1");
		numeros.setCardinalidadMinima("1");
		patente.addAtributo(letras);
		patente.addAtributo(numeros);
		auto.addAtributo(patente);
		Entidad.Identificador idAuto= auto.new Identificador(auto);
		idAuto.addAtributo(patente);
		auto.addIdentificador(idAuto);
		der.agregar(auto);
		
		diagramaLogico=conversor.convertir(der);
		
		List<Tabla> tablas=diagramaLogico.getTablas();
		
		Assert.assertTrue(tablas.size()==1);
		Tabla tablaAuto= tablas.get(0);
		
		Tabla expected=new Tabla("Auto");
		expected.addClavePrimaria("patente-letras");
		expected.addClavePrimaria("patente-numeros");
		expected.addAtributo("patente-letras");
		expected.addAtributo("patente-numeros");
		
		Assert.assertEquals(expected, tablaAuto);
	
	}
}
