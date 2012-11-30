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
import mereditor.modelo.Relacion.EntidadRelacion;
import mereditor.modelo.Relacion.TipoRelacion;
import mereditor.modelo.Relacion;
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
	
	@Test
	public void convertirEntidadesRelacionadasUnoAMuchos(){
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
		
		Entidad persona=new Entidad("Persona");
		persona.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo dni= new Atributo("dni");
		dni.setTipo(TipoAtributo.CARACTERIZACION);
		dni.setCardinalidadMaxima("1");
		dni.setCardinalidadMinima("1");
		persona.addAtributo(dni);
		
		Entidad.Identificador idPersona= persona.new Identificador(persona);
		idPersona.addAtributo(dni);
		persona.addIdentificador(idPersona);
		
		Atributo nombre= new Atributo("nombre");
		nombre.setTipo(TipoAtributo.CARACTERIZACION);
		nombre.setCardinalidadMaxima("1");
		nombre.setCardinalidadMinima("1");
		persona.addAtributo(nombre);
		
		der.agregar(persona);
		
		Relacion esDuenia=new Relacion("EsDuenia");
		esDuenia.setTipo(TipoRelacion.ASOCIACION);
		esDuenia.addParticipante(esDuenia.new EntidadRelacion(esDuenia, persona, "duenia", "0", "N"));
		esDuenia.addParticipante(esDuenia.new EntidadRelacion(esDuenia, auto, "dueniaDe", "1", "1"));
		Atributo fecha= new Atributo("fecha");
		fecha.setTipo(TipoAtributo.CARACTERIZACION);
		fecha.setCardinalidadMaxima("1");
		fecha.setCardinalidadMinima("1");
		esDuenia.addAtributo(fecha);
		
		der.agregar(esDuenia);
		
		diagramaLogico=conversor.convertir(der);
		
		List<Tabla> tablas=diagramaLogico.getTablas();
		
		Tabla tablaAuto=new Tabla("Auto");
		tablaAuto.addAtributo("patente");
		tablaAuto.addAtributo("color");
		tablaAuto.addAtributo("EsDuenia-Persona-dni");
		tablaAuto.addAtributo("EsDuenia-fecha");
		tablaAuto.addClavePrimaria("patente");
		tablaAuto.addClaveForanea("EsDuenia-Persona-dni", "Persona");
		
		Tabla tablaPersona=new Tabla("Persona");
		tablaPersona.addAtributo("dni");
		tablaPersona.addAtributo("nombre");
		tablaPersona.addClavePrimaria("dni");
		
		
		
		Assert.assertTrue(tablas.contains(tablaAuto));
		Assert.assertTrue(tablas.contains(tablaPersona));

		Assert.assertTrue(tablas.size()==2);

	}
	@Test
	public void convertirEntidadesRelacionadasMuchosAMuchos(){
		Diagrama der=new Diagrama(null);

		Entidad alumno= new Entidad("Alumno");
		alumno.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo padron= new Atributo("padron");
		padron.setTipo(TipoAtributo.CARACTERIZACION);
		padron.setCardinalidadMaxima("1");
		padron.setCardinalidadMinima("1");
		alumno.addAtributo(padron);
		
		Entidad.Identificador idAlumno= alumno.new Identificador(alumno);
		idAlumno.addAtributo(padron);
		alumno.addIdentificador(idAlumno);
		
		Atributo nombre= new Atributo("nombre");
		nombre.setTipo(TipoAtributo.CARACTERIZACION);
		nombre.setCardinalidadMaxima("1");
		nombre.setCardinalidadMinima("1");
		alumno.addAtributo(nombre);
		
		der.agregar(alumno);
		
		Entidad materia=new Entidad("Materia");
		materia.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo codigo= new Atributo("codigo");
		codigo.setTipo(TipoAtributo.CARACTERIZACION);
		codigo.setCardinalidadMaxima("1");
		codigo.setCardinalidadMinima("1");
		materia.addAtributo(codigo);
		
		Entidad.Identificador idMateria= materia.new Identificador(materia);
		idMateria.addAtributo(codigo);
		materia.addIdentificador(idMateria);
		
		Atributo nombreMateria= new Atributo("nombre");
		nombreMateria.setTipo(TipoAtributo.CARACTERIZACION);
		nombreMateria.setCardinalidadMaxima("1");
		nombreMateria.setCardinalidadMinima("1");
		materia.addAtributo(nombreMateria);
		
		der.agregar(materia);
		
		Relacion aprobo=new Relacion("Aprobo");
		aprobo.setTipo(TipoRelacion.ASOCIACION);
		aprobo.addParticipante(aprobo.new EntidadRelacion(aprobo, alumno, "quien", "0", "N"));
		aprobo.addParticipante(aprobo.new EntidadRelacion(aprobo, materia, "que", "0", "N"));
		Atributo fecha= new Atributo("fecha");
		fecha.setTipo(TipoAtributo.CARACTERIZACION);
		fecha.setCardinalidadMaxima("1");
		fecha.setCardinalidadMinima("1");
		aprobo.addAtributo(fecha);
		
		der.agregar(aprobo);
		
		diagramaLogico=conversor.convertir(der);
		
List<Tabla> tablas=diagramaLogico.getTablas();
		
		Tabla tablaAlumno=new Tabla("Alumno");
		tablaAlumno.addAtributo("padron");
		tablaAlumno.addAtributo("nombre");
		tablaAlumno.addClavePrimaria("padron");
		
		Tabla tablaMateria=new Tabla("Materia");
		tablaMateria.addAtributo("codigo");
		tablaMateria.addAtributo("nombre");
		tablaMateria.addClavePrimaria("codigo");
		
		Tabla tablaAprobo=new Tabla("Aprobo");
		tablaAprobo.addAtributo("Materia-codigo");
		tablaAprobo.addAtributo("Alumno-padron");
		tablaAprobo.addAtributo("fecha");
		tablaAprobo.addClavePrimaria("Materia-codigo");
		tablaAprobo.addClavePrimaria("Alumno-padron");
		tablaAprobo.addClaveForanea("Materia-codigo", "Materia");
		tablaAprobo.addClaveForanea("Alumno-padron", "Alumno");
		
		
		Assert.assertTrue(tablas.contains(tablaAlumno));
		Assert.assertTrue(tablas.contains(tablaMateria));
		Assert.assertTrue(tablas.contains(tablaAprobo));
		Assert.assertTrue(tablas.size()==3);
	
	}
}
