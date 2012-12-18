package mreleditor.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mereditor.modelo.Atributo;
import mereditor.modelo.DiagramaDER;
import mereditor.modelo.Entidad;
import mereditor.modelo.Atributo.TipoAtributo;
import mereditor.modelo.Entidad.TipoEntidad;
import mereditor.modelo.Jerarquia;
import mereditor.modelo.Jerarquia.TipoJerarquia;
import mereditor.modelo.Relacion.TipoRelacion;
import mereditor.modelo.Relacion;
import mreleditor.conversor.ConversorDERaLogico;
import mreleditor.modelo.DiagramaLogico;
import mreleditor.modelo.Tabla;

public class ConversorTest {

	ConversorDERaLogico conversor;
	DiagramaLogico diagramaLogico;

	@Before
	public void init() {
		conversor = ConversorDERaLogico.getInstance();
		diagramaLogico = new DiagramaLogico();
	}

	@Test
	public void conversionDeUnaEntidadSimple() {
		DiagramaDER der = new DiagramaDER(null);
		Entidad auto = new Entidad("Auto");
		auto.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo patente = new Atributo("patente");
		patente.setTipo(TipoAtributo.CARACTERIZACION);
		patente.setCardinalidadMaxima("1");
		patente.setCardinalidadMinima("1");
		auto.addAtributo(patente);

		Entidad.Identificador idAuto = auto.new Identificador(auto);
		idAuto.addAtributo(patente);
		auto.addIdentificador(idAuto);

		Atributo color = new Atributo("color");
		color.setTipo(TipoAtributo.CARACTERIZACION);
		color.setCardinalidadMaxima("1");
		color.setCardinalidadMinima("1");
		auto.addAtributo(color);

		der.agregar(auto);

		diagramaLogico = conversor.convertir(der);

		List<Tabla> tablas = diagramaLogico.getTablas();

		Assert.assertTrue(tablas.size() == 1);
		Tabla tablaAuto = tablas.get(0);
		Tabla expected = new Tabla("Auto");
		expected.addClavePrimaria("patente");
		expected.addAtributo("patente");
		expected.addAtributo("color");

		Assert.assertEquals(expected, tablaAuto);

	}

	@Test
	public void convertirEntidadConAtributoCompuesto() {
		DiagramaDER der = new DiagramaDER(null);
		Entidad auto = new Entidad("Auto");
		auto.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo patente = new Atributo("patente");
		patente.setTipo(TipoAtributo.CARACTERIZACION);
		patente.setCardinalidadMaxima("1");
		patente.setCardinalidadMinima("1");
		auto.addAtributo(patente);

		Entidad.Identificador idAuto = auto.new Identificador(auto);
		idAuto.addAtributo(patente);
		auto.addIdentificador(idAuto);
		Atributo duenio = new Atributo("duenio");
		duenio.setCardinalidadMaxima("1");
		duenio.setCardinalidadMinima("1");
		duenio.setTipo(TipoAtributo.CARACTERIZACION);
		Atributo nombreDuenio = new Atributo("nombre");
		duenio.setCardinalidadMaxima("1");
		duenio.setCardinalidadMinima("1");
		duenio.setTipo(TipoAtributo.CARACTERIZACION);
		Atributo dniDuenio = new Atributo("dni");
		duenio.setCardinalidadMaxima("1");
		duenio.setCardinalidadMinima("1");
		duenio.setTipo(TipoAtributo.CARACTERIZACION);

		duenio.addAtributo(nombreDuenio);
		duenio.addAtributo(dniDuenio);

		auto.addAtributo(duenio);

		der.agregar(auto);

		diagramaLogico = conversor.convertir(der);

		List<Tabla> tablas = diagramaLogico.getTablas();

		Assert.assertTrue(tablas.size() == 1);
		Tabla tablaAuto = tablas.get(0);

		Tabla expected = new Tabla("Auto");
		expected.addClavePrimaria("patente");
		expected.addAtributo("patente");
		expected.addAtributo("duenio-nombre");
		expected.addAtributo("duenio-dni");

		Assert.assertEquals(expected, tablaAuto);

	}

	@Test
	public void convertirEntidadConAtributoCompuestoComoClave() {
		DiagramaDER der = new DiagramaDER(null);
		Entidad auto = new Entidad("Auto");
		auto.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo patente = new Atributo("patente");
		patente.setTipo(TipoAtributo.CARACTERIZACION);
		patente.setCardinalidadMaxima("1");
		patente.setCardinalidadMinima("1");
		Atributo letras = new Atributo("letras");
		letras.setTipo(TipoAtributo.CARACTERIZACION);
		letras.setCardinalidadMaxima("1");
		letras.setCardinalidadMinima("1");
		Atributo numeros = new Atributo("numeros");
		numeros.setTipo(TipoAtributo.CARACTERIZACION);
		numeros.setCardinalidadMaxima("1");
		numeros.setCardinalidadMinima("1");
		patente.addAtributo(letras);
		patente.addAtributo(numeros);
		auto.addAtributo(patente);
		Entidad.Identificador idAuto = auto.new Identificador(auto);
		idAuto.addAtributo(patente);
		auto.addIdentificador(idAuto);
		der.agregar(auto);

		diagramaLogico = conversor.convertir(der);

		List<Tabla> tablas = diagramaLogico.getTablas();

		Assert.assertTrue(tablas.size() == 1);
		Tabla tablaAuto = tablas.get(0);

		Tabla expected = new Tabla("Auto");
		expected.addClavePrimaria("patente-letras");
		expected.addClavePrimaria("patente-numeros");
		expected.addAtributo("patente-letras");
		expected.addAtributo("patente-numeros");

		Assert.assertEquals(expected, tablaAuto);

	}

	@Test
	public void convertirEntidadesRelacionadasUnoAMuchos() {
		DiagramaDER der = new DiagramaDER(null);

		Entidad auto = new Entidad("Auto");
		auto.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo patente = new Atributo("patente");
		patente.setTipo(TipoAtributo.CARACTERIZACION);
		patente.setCardinalidadMaxima("1");
		patente.setCardinalidadMinima("1");
		auto.addAtributo(patente);

		Entidad.Identificador idAuto = auto.new Identificador(auto);
		idAuto.addAtributo(patente);
		auto.addIdentificador(idAuto);

		Atributo color = new Atributo("color");
		color.setTipo(TipoAtributo.CARACTERIZACION);
		color.setCardinalidadMaxima("1");
		color.setCardinalidadMinima("1");
		auto.addAtributo(color);

		der.agregar(auto);

		Entidad persona = new Entidad("Persona");
		persona.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo dni = new Atributo("dni");
		dni.setTipo(TipoAtributo.CARACTERIZACION);
		dni.setCardinalidadMaxima("1");
		dni.setCardinalidadMinima("1");
		persona.addAtributo(dni);

		Entidad.Identificador idPersona = persona.new Identificador(persona);
		idPersona.addAtributo(dni);
		persona.addIdentificador(idPersona);

		Atributo nombre = new Atributo("nombre");
		nombre.setTipo(TipoAtributo.CARACTERIZACION);
		nombre.setCardinalidadMaxima("1");
		nombre.setCardinalidadMinima("1");
		persona.addAtributo(nombre);

		der.agregar(persona);

		Relacion esDuenia = new Relacion("EsDuenia");
		esDuenia.setTipo(TipoRelacion.ASOCIACION);
		esDuenia.addParticipante(esDuenia.new EntidadRelacion(esDuenia, persona, "duenia", "0", "N"));
		esDuenia.addParticipante(esDuenia.new EntidadRelacion(esDuenia, auto, "dueniaDe", "1", "1"));
		Atributo fecha = new Atributo("fecha");
		fecha.setTipo(TipoAtributo.CARACTERIZACION);
		fecha.setCardinalidadMaxima("1");
		fecha.setCardinalidadMinima("1");
		esDuenia.addAtributo(fecha);

		der.agregar(esDuenia);

		diagramaLogico = conversor.convertir(der);

		List<Tabla> tablas = diagramaLogico.getTablas();

		Tabla tablaAuto = new Tabla("Auto");
		tablaAuto.addAtributo("patente");
		tablaAuto.addAtributo("color");
		tablaAuto.addAtributo("EsDuenia-Persona-dni");
		tablaAuto.addAtributo("EsDuenia-fecha");
		tablaAuto.addClavePrimaria("patente");
		tablaAuto.addClaveForanea("EsDuenia-Persona-dni", "Persona");

		Tabla tablaPersona = new Tabla("Persona");
		tablaPersona.addAtributo("dni");
		tablaPersona.addAtributo("nombre");
		tablaPersona.addClavePrimaria("dni");

		Assert.assertTrue(tablas.contains(tablaAuto));
		Assert.assertTrue(tablas.contains(tablaPersona));

		Assert.assertTrue(tablas.size() == 2);

	}

	@Test
	public void convertirEntidadesRelacionadasMuchosAMuchos() {
		DiagramaDER der = new DiagramaDER(null);

		Entidad alumno = new Entidad("Alumno");
		alumno.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo padron = new Atributo("padron");
		padron.setTipo(TipoAtributo.CARACTERIZACION);
		padron.setCardinalidadMaxima("1");
		padron.setCardinalidadMinima("1");
		alumno.addAtributo(padron);

		Entidad.Identificador idAlumno = alumno.new Identificador(alumno);
		idAlumno.addAtributo(padron);
		alumno.addIdentificador(idAlumno);

		Atributo nombre = new Atributo("nombre");
		nombre.setTipo(TipoAtributo.CARACTERIZACION);
		nombre.setCardinalidadMaxima("1");
		nombre.setCardinalidadMinima("1");
		alumno.addAtributo(nombre);

		der.agregar(alumno);

		Entidad materia = new Entidad("Materia");
		materia.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo codigo = new Atributo("codigo");
		codigo.setTipo(TipoAtributo.CARACTERIZACION);
		codigo.setCardinalidadMaxima("1");
		codigo.setCardinalidadMinima("1");
		materia.addAtributo(codigo);

		Entidad.Identificador idMateria = materia.new Identificador(materia);
		idMateria.addAtributo(codigo);
		materia.addIdentificador(idMateria);

		Atributo nombreMateria = new Atributo("nombre");
		nombreMateria.setTipo(TipoAtributo.CARACTERIZACION);
		nombreMateria.setCardinalidadMaxima("1");
		nombreMateria.setCardinalidadMinima("1");
		materia.addAtributo(nombreMateria);

		der.agregar(materia);

		Relacion aprobo = new Relacion("Aprobo");
		aprobo.setTipo(TipoRelacion.ASOCIACION);
		aprobo.addParticipante(aprobo.new EntidadRelacion(aprobo, alumno, "quien", "0", "N"));
		aprobo.addParticipante(aprobo.new EntidadRelacion(aprobo, materia, "que", "0", "N"));
		Atributo fecha = new Atributo("fecha");
		fecha.setTipo(TipoAtributo.CARACTERIZACION);
		fecha.setCardinalidadMaxima("1");
		fecha.setCardinalidadMinima("1");
		aprobo.addAtributo(fecha);

		der.agregar(aprobo);

		diagramaLogico = conversor.convertir(der);

		List<Tabla> tablas = diagramaLogico.getTablas();

		Tabla tablaAlumno = new Tabla("Alumno");
		tablaAlumno.addAtributo("padron");
		tablaAlumno.addAtributo("nombre");
		tablaAlumno.addClavePrimaria("padron");

		Tabla tablaMateria = new Tabla("Materia");
		tablaMateria.addAtributo("codigo");
		tablaMateria.addAtributo("nombre");
		tablaMateria.addClavePrimaria("codigo");

		Tabla tablaAprobo = new Tabla("Aprobo");
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
		Assert.assertTrue(tablas.size() == 3);

	}
	@Test
	public void convertirJerarquiaColapsandoEnPadre(){
		DiagramaDER der = new DiagramaDER(null);

		Entidad persona = new Entidad("Persona");
		persona.setTipo(TipoEntidad.MAESTRA_COSA);
		
		Atributo dni = new Atributo("dni");
		dni.setTipo(TipoAtributo.CARACTERIZACION);
		dni.setCardinalidadMaxima("1");
		dni.setCardinalidadMinima("1");
		persona.addAtributo(dni);

		Entidad.Identificador idPersona = persona.new Identificador(persona);
		idPersona.addAtributo(dni);
		persona.addIdentificador(idPersona);

		Atributo nombre = new Atributo("nombre");
		nombre.setTipo(TipoAtributo.CARACTERIZACION);
		nombre.setCardinalidadMaxima("1");
		nombre.setCardinalidadMinima("1");
		persona.addAtributo(nombre);
		
		Atributo direccion = new Atributo("direccion");
		direccion.setTipo(TipoAtributo.CARACTERIZACION);
		direccion.setCardinalidadMaxima("1");
		direccion.setCardinalidadMinima("1");
		persona.addAtributo(direccion);
		
		Atributo telefono = new Atributo("telefono");
		telefono.setTipo(TipoAtributo.CARACTERIZACION);
		telefono.setCardinalidadMaxima("1");
		telefono.setCardinalidadMinima("1");
		persona.addAtributo(telefono);
		
		der.agregar(persona);
		
		Entidad alumno = new Entidad("Alumno");
		alumno.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo padron = new Atributo("padron");
		padron.setTipo(TipoAtributo.CARACTERIZACION);
		padron.setCardinalidadMaxima("1");
		padron.setCardinalidadMinima("1");
		alumno.addAtributo(padron);

		Entidad.Identificador idAlumno = alumno.new Identificador(alumno);
		idAlumno.addAtributo(padron);
		alumno.addIdentificador(idAlumno);
		
		der.agregar(alumno);
		
		Entidad docente = new Entidad("Docente");
		docente.setTipo(TipoEntidad.MAESTRA_COSA);
		Atributo nroLegajo = new Atributo("nroLegajo");
		nroLegajo.setTipo(TipoAtributo.CARACTERIZACION);
		nroLegajo.setCardinalidadMaxima("1");
		nroLegajo.setCardinalidadMinima("1");
		docente.addAtributo(nroLegajo);

		Entidad.Identificador idDocente = docente.new Identificador(docente);
		idDocente.addAtributo(nroLegajo);
		docente.addIdentificador(idDocente);	

		der.agregar(docente);
		
		Jerarquia jerarquia = new Jerarquia();
		jerarquia.setGenerica(persona);
		jerarquia.addDerivada(docente);
		jerarquia.addDerivada(alumno);
		jerarquia.setTipo(TipoJerarquia.TOTAL_SUPERPUESTA);
		
		der.agregar(jerarquia);
		
		diagramaLogico = conversor.convertir(der);

		List<Tabla> tablas = diagramaLogico.getTablas();

		Tabla tablaPersona = new Tabla("Persona");
		tablaPersona.addAtributo("dni");
		tablaPersona.addAtributo("nombre");
		tablaPersona.addAtributo("direccion");
		tablaPersona.addAtributo("telefono");
		tablaPersona.addAtributo("Alumno-padron");
		tablaPersona.addAtributo("Docente-nroLegajo");
		tablaPersona.addClavePrimaria("dni");

		

		Assert.assertTrue(tablas.contains(tablaPersona));

		Assert.assertTrue(tablas.size() == 1);
	}
	@Test
	public void convertirJerarquiaColapsandoEnHijos(){
		DiagramaDER der = new DiagramaDER(null);

		Entidad todo = new Entidad("Todo");
		todo.setTipo(TipoEntidad.MAESTRA_COSA);
		
		Atributo todoIdAt = new Atributo("id");
		todoIdAt.setTipo(TipoAtributo.CARACTERIZACION);
		todoIdAt.setCardinalidadMaxima("1");
		todoIdAt.setCardinalidadMinima("1");
		todo.addAtributo(todoIdAt);

		Entidad.Identificador idTodo = todo.new Identificador(todo);
		idTodo.addAtributo(todoIdAt);
		todo.addIdentificador(idTodo);
		
		Atributo todoAt = new Atributo("todoAtributo");
		todoAt.setTipo(TipoAtributo.CARACTERIZACION);
		todoAt.setCardinalidadMaxima("1");
		todoAt.setCardinalidadMinima("1");
		todo.addAtributo(todoAt);
		
		der.agregar(todo);
		
		Entidad parte1 = new Entidad("Parte1");
		parte1.setTipo(TipoEntidad.MAESTRA_COSA);
		
		Atributo parte1IdAt = new Atributo("parte1Id");
		parte1IdAt.setTipo(TipoAtributo.CARACTERIZACION);
		parte1IdAt.setCardinalidadMaxima("1");
		parte1IdAt.setCardinalidadMinima("1");
		parte1.addAtributo(parte1IdAt);

		Entidad.Identificador idParte1 = parte1.new Identificador(parte1);
		idParte1.addAtributo(parte1IdAt);
		parte1.addIdentificador(idParte1);
		
		Atributo parte1At = new Atributo("parte1Atributo");
		parte1At.setTipo(TipoAtributo.CARACTERIZACION);
		parte1At.setCardinalidadMaxima("1");
		parte1At.setCardinalidadMinima("1");
		parte1.addAtributo(parte1At);
		
		der.agregar(parte1);

		Entidad parte2 = new Entidad("Parte2");
		parte2.setTipo(TipoEntidad.MAESTRA_COSA);
		
		Atributo parte2IdAt = new Atributo("parte2Id");
		parte2IdAt.setTipo(TipoAtributo.CARACTERIZACION);
		parte2IdAt.setCardinalidadMaxima("1");
		parte2IdAt.setCardinalidadMinima("1");
		parte2.addAtributo(parte2IdAt);

		Entidad.Identificador idParte2 = parte2.new Identificador(parte2);
		idParte2.addAtributo(parte2IdAt);
		parte2.addIdentificador(idParte2);
		
		Atributo parte2At = new Atributo("parte2Atributo");
		parte2At.setTipo(TipoAtributo.CARACTERIZACION);
		parte2At.setCardinalidadMaxima("1");
		parte2At.setCardinalidadMinima("1");
		parte2.addAtributo(parte2At);
		
		der.agregar(parte2);
		
		Jerarquia jerarquia = new Jerarquia();
		jerarquia.setGenerica(todo);
		jerarquia.addDerivada(parte1);
		jerarquia.addDerivada(parte2);
		jerarquia.setTipo(TipoJerarquia.TOTAL_EXCLUSIVA);
		
		der.agregar(jerarquia);
		
		diagramaLogico = conversor.convertir(der);

		List<Tabla> tablas = diagramaLogico.getTablas();

		Tabla tablaParte1 = new Tabla("Parte1");
		tablaParte1.addAtributo("id");
		tablaParte1.addAtributo("parte1Id");
		tablaParte1.addAtributo("parte1Atributo");
		tablaParte1.addAtributo("Todo-todoAtributo");
		tablaParte1.addClavePrimaria("id");
		
		Tabla tablaParte2 = new Tabla("Parte2");
		tablaParte2.addAtributo("id");
		tablaParte2.addAtributo("parte2Id");
		tablaParte2.addAtributo("parte2Atributo");
		tablaParte2.addAtributo("Todo-todoAtributo");
		tablaParte2.addClavePrimaria("id");

		

		Assert.assertTrue(tablas.contains(tablaParte1));
		Assert.assertTrue(tablas.contains(tablaParte2));
		Assert.assertTrue(tablas.size() == 2);
		
	}
	
	@Test
	public void convertirEntidadConAtributosPolivalentes(){
		DiagramaDER der = new DiagramaDER(null);

		Entidad persona = new Entidad("Persona");
		persona.setTipo(TipoEntidad.MAESTRA_COSA);
		
		Atributo dni = new Atributo("dni");
		dni.setTipo(TipoAtributo.CARACTERIZACION);
		dni.setCardinalidadMaxima("1");
		dni.setCardinalidadMinima("1");
		persona.addAtributo(dni);

		Entidad.Identificador idPersona = persona.new Identificador(persona);
		idPersona.addAtributo(dni);
		persona.addIdentificador(idPersona);

		Atributo nombre = new Atributo("nombre");
		nombre.setTipo(TipoAtributo.CARACTERIZACION);
		nombre.setCardinalidadMaxima("1");
		nombre.setCardinalidadMinima("1");
		persona.addAtributo(nombre);
		
		Atributo direccion = new Atributo("direccion");
		direccion.setTipo(TipoAtributo.CARACTERIZACION);
		direccion.setCardinalidadMaxima("N");
		direccion.setCardinalidadMinima("1");
		persona.addAtributo(direccion);
		
		Atributo telefono = new Atributo("telefono");
		telefono.setTipo(TipoAtributo.CARACTERIZACION);
		telefono.setCardinalidadMaxima("N");
		telefono.setCardinalidadMinima("1");
		
		Atributo codArea = new Atributo("codArea");
		codArea.setTipo(TipoAtributo.CARACTERIZACION);
		codArea.setCardinalidadMaxima("1");
		codArea.setCardinalidadMinima("1");
		
		Atributo numero = new Atributo("numero");
		numero.setTipo(TipoAtributo.CARACTERIZACION);
		numero.setCardinalidadMaxima("1");
		numero.setCardinalidadMinima("1");
		
		telefono.addAtributo(codArea);
		telefono.addAtributo(numero);
		
		persona.addAtributo(telefono);
		
		der.agregar(persona);
		
		diagramaLogico = conversor.convertir(der);

		List<Tabla> tablas = diagramaLogico.getTablas();
		
		Tabla personaTabla = new Tabla("Persona");
		personaTabla.addAtributo("dni");
		personaTabla.addAtributo("nombre");
		personaTabla.addClavePrimaria("dni");
		
		Tabla telefonoTabla = new Tabla("telefono_de_Persona");
		telefonoTabla.addAtributo("id");
		telefonoTabla.addAtributo("Persona-dni");
		telefonoTabla.addAtributo("telefono-codArea");
		telefonoTabla.addAtributo("telefono-numero");
		telefonoTabla.addClavePrimaria("id");
		telefonoTabla.addClavePrimaria("Persona-dni");
		telefonoTabla.addClaveForanea("Persona-dni", "Persona");
		
		Tabla direccionTabla = new Tabla("direccion_de_Persona");
		direccionTabla.addAtributo("direccion");
		direccionTabla.addAtributo("Persona-dni");
		direccionTabla.addClavePrimaria("direccion");
		direccionTabla.addClavePrimaria("Persona-dni");
		direccionTabla.addClaveForanea("Persona-dni", "Persona");
		
		Assert.assertTrue(tablas.contains(personaTabla));
		Assert.assertTrue(tablas.contains(telefonoTabla));
		Assert.assertTrue(tablas.contains(direccionTabla));
		Assert.assertTrue(tablas.size() == 3);
		
		
	}
	

	
	
}
