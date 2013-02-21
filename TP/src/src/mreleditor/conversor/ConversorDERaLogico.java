package mreleditor.conversor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import mereditor.control.TablaControl;
import mereditor.modelo.Atributo;
import mereditor.modelo.Atributo.TipoAtributo;
import mereditor.modelo.Diagrama;
import mereditor.modelo.Entidad;
import mereditor.modelo.Entidad.Identificador;
import mereditor.modelo.Jerarquia;
import mereditor.modelo.Relacion;
import mereditor.modelo.Relacion.EntidadRelacion;
import mereditor.modelo.base.Componente;
import mreleditor.modelo.DiagramaLogico;
import mreleditor.modelo.Tabla;

public class ConversorDERaLogico {

	private static ConversorDERaLogico instance = null;
	DiagramaLogico diagramaLogico;

	private ConversorDERaLogico() {
		inicializarVariables();

	}

	private void inicializarVariables() {
		der = null;
		raicesProcesadas = new HashSet<Entidad>();
		tipoDeJerarquia = new HashMap<Entidad, Jerarquia.TipoJerarquia>();
		entidadPadre = new HashMap<Entidad, Entidad>();
		relacionesDePadres = new HashSet<Relacion>();
		relacionesProcesadas = new HashSet<Relacion>();
		entidadesBorradas = new HashSet<Entidad>();
		diagramaLogico = new DiagramaLogico();
	}

	public static ConversorDERaLogico getInstance() {
		if (instance == null)
			instance = new ConversorDERaLogico();
		return instance;
	}

	private Diagrama der;

	private enum TipoConversionDeJerarquia {
		COLAPSAR_EN_PADRE, COLAPSAR_EN_HIJOS, SIN_COLAPSAR
	};

	private Set<Entidad> raicesProcesadas;
	private HashMap<Entidad, Jerarquia.TipoJerarquia> tipoDeJerarquia;
	private HashMap<Entidad, Entidad> entidadPadre;
	private Set<Relacion> relacionesDePadres;
	private Set<Relacion> relacionesProcesadas;

	public DiagramaLogico convertir(Diagrama der) {
		inicializarVariables();
		this.der = der;
		diagramaLogico.setNombre(der.getNombre() + " - Logico");
		diagramaLogico.setDer(der);

		// Primero convierte las jerarquias
		for (Entidad raiz : getRaices()) {
			for (Tabla tablaDeJerarquia : convertirJerarquia(raiz)) {
				diagramaLogico.agregar(tablaDeJerarquia);
			}
		}
		for (Relacion relacion : relacionesDePadres) {
			for (Tabla tabla : convertirRelacionDePadre(relacion)) {
				diagramaLogico.agregar(tabla);
			}
		}
		for (Componente componente : der.getComponentes()) {
			if (componente.es(Entidad.class)) {
				if (!((Entidad) componente).esJerarquica()) {
					// No forma una jerarquia
					Tabla tablaEntidad = convertirEntidad((Entidad) componente);
					diagramaLogico.agregar(tablaEntidad);
				}
			}
		}
		for (Componente componente : der.getComponentes()) {
			if (componente.es(Relacion.class)) {

				if (!relacionesProcesadas.contains((Relacion) componente)) {
					Tabla tabla = convertirRelacion((Relacion) componente);
					diagramaLogico.agregar(tabla);
				}
			}

		}

		return diagramaLogico;

	}

	/*
	 * Interfaz privada
	 */

	private Tabla convertirEntidad(Entidad entidad) {
		Tabla tabla = new TablaControl(entidad.getNombre());

		agregarPK(entidad, tabla);
		agregarAtributos(entidad, tabla);
		agregarEntidadesRelacionadas(entidad, tabla);

		return tabla;
	}

	private void agregarPK(Entidad entidad, Tabla tabla) {
		agregarPK(entidad, tabla, "");
	}

	private void agregarPK(Entidad entidad, Tabla tabla, String prefijo) {
		Entidad entidadPK;

		entidadPK = entidadPadre.get(entidad);
		if (entidadPK == null) {
			entidadPK = entidad;
		}

		tabla.addClavePrimaria(construirPK(entidadPK, prefijo));
	}

	private ArrayList<String> construirPK(Entidad entidad, String prefijo) {
		ArrayList<String> PK = new ArrayList<String>();

		for (Identificador identificador : entidad.getIdentificadores()) {
			if (identificador.isInterno()) {
				// identificador de atributos
				PK = construirPKdeAtributos(identificador, prefijo);
			} else if (identificador.isExterno()) {
				// identificado por otras entidades
				PK = construirPKdeEntidades(identificador, prefijo);
			} else {
				// identificado por entidades y atributos
				PK = construirPKdeAtributos(identificador, prefijo);
				PK.addAll(construirPKdeEntidades(identificador, prefijo));
			}
		}
		return PK;
	}

	private ArrayList<String> construirPKdeAtributos(Identificador identificador, String prefijo) {
		ArrayList<String> PK = new ArrayList<String>();
		for (Atributo atributoId : identificador.getAtributos()) {
			if (atributoId.getCardinalidadMaxima().equals("1")) {
				// monovalente
				PK.addAll(construirAtributos(atributoId, prefijo));
			}

		}
		return PK;
	}

	private ArrayList<String> construirPKdeEntidades(Identificador identificador, String prefijo) {
		ArrayList<String> PK = new ArrayList<String>();
		for (Entidad entidadId : identificador.getEntidades()) {
			PK.addAll(construirPK(entidadId, prefijo + entidadId.getNombre() + "-"));
		}
		return PK;
	}

	/*
	 * Construye una lista de atributos componentes del atributoPadre
	 */
	private ArrayList<String> construirAtributos(Atributo atributoPadre) {
		return construirAtributos(atributoPadre, "");
	}

	private ArrayList<String> construirAtributos(Atributo atributoPadre, String prefijo) {
		ArrayList<String> atributos = new ArrayList<String>();

		if (!atributoPadre.esCompuesto())
			atributos.add(prefijo + atributoPadre.getNombre());
		else {
			for (Atributo atributoComponente : atributoPadre.getAtributos()) {
				atributos.addAll(construirAtributos(atributoComponente, atributoPadre.getNombre() + "-"));
			}
		}

		return atributos;
	}

	private void agregarAtributos(Entidad entidad, Tabla tabla) {
		agregarAtributos(entidad, tabla, "");
	}

	private void agregarAtributos(Entidad entidad, Tabla tabla, String prefijo) {
		agregarAtributos(entidad, tabla, prefijo, true);
	}

	private void agregarAtributos(Entidad entidad, Tabla tabla, String prefijo, boolean incluirPK) {
		for (Atributo atributo : entidad.getAtributos()) {
			if (atributo.getCardinalidadMaxima().equals("1")) {
				// monovalente
				if (atributo.getTipo() == TipoAtributo.CARACTERIZACION) {
					// solo se convierten los de caracterizacion
					if (incluirPK || !construirPK(entidad, "").contains(atributo.getNombre()))
						agregarAtributo(atributo, tabla, prefijo);
				}
			} else {
				// polivalente
				Tabla tablaAtributo = new TablaControl(atributo.getNombre() + "_de_" + entidad.getNombre());
				/*String prefijoPoli;
				if (entidadesBorradas.contains(entidad) && entidadPadre.get(entidad) != null) {

					prefijoPoli = entidadPadre.get(entidad).getNombre();

				} else {
					prefijoPoli = entidad.getNombre();
				}*/
				agregarPK(entidad, tablaAtributo, entidad.getNombre() + "-");

				agregarFK(entidad, tablaAtributo);

				if (atributo.esCompuesto()) {
					tablaAtributo.addClavePrimaria("id");
					agregarAtributo(atributo, tablaAtributo);

				} else {
					tablaAtributo.addClavePrimaria(atributo.getNombre());
				}
				diagramaLogico.agregar(tablaAtributo);
			}
		}
	}

	private void agregarAtributo(Atributo atributo, Tabla tabla) {
		agregarAtributo(atributo, tabla, "");
	}

	private void agregarAtributo(Atributo atributo, Tabla tabla, String prefijo) {
		for (String atributoComponente : construirAtributos(atributo, prefijo)) {
			tabla.addAtributo(atributoComponente);
		}
	}

	private int getCantidadAtributosMonovalentes(Entidad entidad) {
		int cant = 0;

		for (Atributo atributo : entidad.getAtributos()) {
			if (atributo.getCardinalidadMaxima().equals("1")) {
				// monovalente
				if (atributo.getTipo() == TipoAtributo.CARACTERIZACION) {
					cant += construirAtributos(atributo).size();
				}
			}
		}
		return cant;
	}

	private void agregarFK(Entidad entidad, Tabla tabla) {
		agregarFK(entidad, tabla, "");
	}

	private Set<Entidad> entidadesBorradas;

	private void agregarFK(Entidad entidad, Tabla tabla, String prefijo) {
		String nombreTabla;
		Entidad entidadPK;

		boolean borrada = entidadesBorradas.contains(entidad);
		if (borrada && entidadPadre.get(entidad) != null) {
			// borrada por colapso en padre
			entidadPK = entidadPadre.get(entidad);
			nombreTabla = entidadPK.getNombre();

		} else {
			entidadPK = entidadPadre.get(entidad);
			if (entidadPK == null) {
				entidadPK = entidad;
			}
			nombreTabla = entidad.getNombre();
		}
		tabla.addClaveForanea(construirPK(entidadPK, prefijo + entidad.getNombre() + "-"), nombreTabla);
	}

	private void agregarFKDePadre(Entidad padre, Tabla tabla, String nombreHijo) {
		agregarFKDePadre(padre, tabla, nombreHijo, "");
	}

	private void agregarFKDePadre(Entidad padre, Tabla tabla, String nombreHijo, String prefijo) {
		tabla.addClaveForanea(construirPK(padre, prefijo + nombreHijo + "-"), nombreHijo);
	}

	private void agregarEntidadesRelacionadas(Entidad entidad, Tabla tabla) {

		for (Relacion relacion : entidad.getRelaciones()) {
			if (relacion.getParticipantes().size() == 2) {
				Iterator<EntidadRelacion> it = relacion.getParticipantes().iterator();
				EntidadRelacion entidadRelacion1 = it.next();
				EntidadRelacion entidadRelacion2 = it.next();

				if (entidadRelacion1.getEntidad().equals(entidad)
						&& entidadRelacion1.getCardinalidadMinima().equals("1")
						&& entidadRelacion1.getCardinalidadMaxima().equals("1")) {
					if (!relacionesProcesadas.contains(relacion)) {

						agregarFK(entidadRelacion2.getEntidad(), tabla, relacion.getNombre() + "-");
						agregarAtributos(relacion, tabla, relacion.getNombre() + "-");

						relacionesProcesadas.add(relacion);
					}

				} else if (entidadRelacion2.getEntidad().equals(entidad)
						&& entidadRelacion2.getCardinalidadMinima().equals("1")
						&& entidadRelacion2.getCardinalidadMaxima().equals("1")) {
					if (!relacionesProcesadas.contains(relacion)) {

						agregarFK(entidadRelacion1.getEntidad(), tabla, relacion.getNombre() + "-");
						agregarAtributos(relacion, tabla, relacion.getNombre() + "-");
						relacionesProcesadas.add(relacion);
					}
				}
			}
		}
	}

	private void agregarAtributos(Relacion relacion, Tabla tabla) {
		agregarAtributos(relacion, tabla, "");
	}

	private void agregarAtributos(Relacion relacion, Tabla tabla, String prefijo) {
		for (Atributo atributo : relacion.getAtributos()) {
			agregarAtributo(atributo, tabla, prefijo);
		}
	}

	private Tabla convertirRelacion(Relacion relacion) {
		Tabla tabla = new TablaControl(relacion.getNombre());
		agregarPK(relacion, tabla);
		agregarFK(relacion, tabla);
		agregarAtributos(relacion, tabla);
		return tabla;

	}

	private Set<Tabla> convertirRelacionDePadre(Relacion relacion) {
		Set<Tabla> tablas = new HashSet<Tabla>();
		ArrayList<ArrayList<Entidad>> matrizHijos = new ArrayList<ArrayList<Entidad>>();
		ArrayList<EntidadRelacion> participantes = new ArrayList<EntidadRelacion>();

		participantes.addAll(relacion.getParticipantes());
		for (EntidadRelacion entidadRelacion : participantes) {
			Entidad entidad = entidadRelacion.getEntidad();
			if (entidadesBorradas.contains(entidad) && entidadPadre.get(entidad) == null) {
				ArrayList<Entidad> hijos = new ArrayList<Entidad>();
				hijos.addAll(entidad.getDerivadas());
				matrizHijos.add(hijos);
			}
		}
		int indices[] = new int[matrizHijos.size()];
		Entidad hijos[] = new Entidad[matrizHijos.size()];

		for (int i = 0; i < matrizHijos.size(); i++) {
			indices[i] = 0;
		}
		boolean finDeConversion = false;
		int j = 0;
		while (!finDeConversion) {
			j++;
			for (int i = 0; i < matrizHijos.size(); i++) {
				hijos[i] = matrizHijos.get(i).get(indices[i]);
			}
			Tabla tabla = new TablaControl(relacion.getNombre() + j);
			agregarPKDeRelacionDePadres(participantes, hijos, tabla);
			agregarFKDeRelacionDePadres(participantes, hijos, tabla);
			agregarAtributos(relacion, tabla);
			tablas.add(tabla);
			finDeConversion = true;
			for (int i = 0; i < matrizHijos.size(); i++) {
				if (indices[i] == matrizHijos.get(i).size() - 1) {
					indices[i] = 0;
				} else {
					indices[i]++;
					finDeConversion = false;
					break;
				}
			}
		}

		relacionesProcesadas.add(relacion);
		return tablas;
	}

	private void agregarPKDeRelacionDePadres(ArrayList<EntidadRelacion> participantes, Entidad hijos[], Tabla tabla) {
		int i = 0;
		boolean PKagregada = false;

		Entidad entidadFinal = null;
		for (EntidadRelacion entidadRelacion : participantes) {

			Entidad entidad = entidadRelacion.getEntidad();

			if (entidadesBorradas.contains(entidad) && entidadPadre.get(entidad) == null) {
				entidadFinal = hijos[i];
				i++;
			} else {
				entidadFinal = entidad;
			}
			for (EntidadRelacion entidadRelacion2 : participantes) {
				if (!entidadRelacion2.getEntidad().getNombre().equals(entidadRelacion.getEntidad().getNombre())) {
					String cardinalidad = entidadRelacion2.getCardinalidadMinima()
							+ entidadRelacion2.getCardinalidadMaxima();
					if (!cardinalidad.equals("01") && !cardinalidad.equals("11")) {

						agregarPK(entidadFinal, tabla, entidadFinal.getNombre() + "-");
						PKagregada = true;
					}
				}
			}
		}
		if (PKagregada == false) {
			agregarPK(entidadFinal, tabla, entidadFinal.getNombre() + "-");
		}

	}

	private void agregarFKDeRelacionDePadres(ArrayList<EntidadRelacion> participantes, Entidad hijos[], Tabla tabla) {
		int i = 0;

		for (EntidadRelacion entidadRelacion : participantes) {
			Entidad entidad = entidadRelacion.getEntidad();
			Entidad entidadFinal;
			if (entidadesBorradas.contains(entidad) && entidadPadre.get(entidad) == null) {
				entidadFinal = hijos[i];
				i++;
			} else {
				entidadFinal = entidad;
			}

			agregarFK(entidadFinal, tabla);

		}
	}

	private void agregarPK(Relacion relacion, Tabla tabla) {
		boolean PKagregada = false;
		EntidadRelacion ultimaEntidadRelacion = null;
		for (EntidadRelacion entidadRelacion : relacion.getParticipantes()) {
			ultimaEntidadRelacion = entidadRelacion;
			for (EntidadRelacion entidadRelacion2 : relacion.getParticipantes()) {
				if (!entidadRelacion2.getEntidad().getNombre().equals(entidadRelacion.getEntidad().getNombre())) {
					String cardinalidad = entidadRelacion2.getCardinalidadMinima()
							+ entidadRelacion2.getCardinalidadMaxima();
					if (!cardinalidad.equals("01") && !cardinalidad.equals("11")) {
						agregarPK(entidadRelacion.getEntidad(), tabla, entidadRelacion.getEntidad().getNombre() + "-");
						PKagregada = true;
					}
				}
			}
		}
		if (PKagregada == false)
			agregarPK(ultimaEntidadRelacion.getEntidad(), tabla, ultimaEntidadRelacion.getEntidad().getNombre() + "-");

	}

	private void agregarFK(Relacion relacion, Tabla tabla) {
		for (EntidadRelacion entidadRelacion : relacion.getParticipantes()) {
			agregarFK(entidadRelacion.getEntidad(), tabla);
		}
	}

	private ArrayList<Entidad> getRaices() {
		ArrayList<Entidad> raices = new ArrayList<Entidad>();
		for (Jerarquia jerarquia : der.getJerarquias(true)) {
			if (raicesProcesadas.contains(jerarquia.getRaiz()))
				continue;
			procesarRaiz(jerarquia);
			raices.add(jerarquia.getRaiz());
		}
		return raices;

	}

	private ArrayList<Tabla> convertirJerarquia(Entidad raiz) {
		ArrayList<Tabla> tablas = new ArrayList<Tabla>();
		switch (getTipoDeConversion(raiz)) {
		case COLAPSAR_EN_PADRE:
			tablas = convertirColapsandoEnPadre(raiz);
			break;
		case COLAPSAR_EN_HIJOS:
			tablas = convertirColapsandoEnHijos(raiz);
			break;
		case SIN_COLAPSAR:
			tablas = convertirSinColapsar(raiz);
			break;
		}
		return tablas;
	}

	private void procesarRaiz(Jerarquia jerarquia) {

		Entidad raiz = jerarquia.getRaiz();
		Stack<Entidad> nodosSinProcesar = new Stack<Entidad>();
		nodosSinProcesar.push(raiz);

		while (!nodosSinProcesar.empty()) {
			Entidad nodoPadre = nodosSinProcesar.pop();
			for (Entidad nodoHijo : nodoPadre.getDerivadas()) {
				entidadPadre.put(nodoHijo, nodoPadre);
				nodosSinProcesar.push(nodoHijo);
			}
		}
		raicesProcesadas.add(raiz);
		if (raiz.getNivel() == 1)
			tipoDeJerarquia.put(raiz, jerarquia.getTipo());

	}

	private TipoConversionDeJerarquia getTipoDeConversion(Entidad raiz) {
		int pesoDePadres = calcularPesoPadres(raiz);
		int pesoDeHijos = calcularPesoHijos(raiz);

		if (pesoDePadres > 10 && pesoDeHijos > 10)
			return TipoConversionDeJerarquia.SIN_COLAPSAR;
		if (pesoDePadres < pesoDeHijos)
			return TipoConversionDeJerarquia.COLAPSAR_EN_HIJOS;

		return TipoConversionDeJerarquia.COLAPSAR_EN_PADRE;

	}

	private int calcularPesoPadres(Entidad raiz) {
		Jerarquia.TipoJerarquia tipo = tipoDeJerarquia.get(raiz);
		if (tipo == null || tipo != Jerarquia.TipoJerarquia.TOTAL_EXCLUSIVA)
			return Integer.MAX_VALUE;
		int peso = getCantidadAtributosMonovalentes(raiz);
		peso += 4 * raiz.getRelaciones().size();
		return peso;
	}

	private int calcularPesoHijos(Entidad raiz) {
		int peso = 0;
		Stack<Entidad> nodosSinProcesar = new Stack<Entidad>();
		nodosSinProcesar.push(raiz);

		while (!nodosSinProcesar.empty()) {
			Entidad nodoPadre = nodosSinProcesar.pop();
			for (Entidad nodoHijo : nodoPadre.getDerivadas()) {
				peso += getCantidadAtributosMonovalentes(nodoHijo);
				peso += 2 * nodoHijo.getRelaciones().size();
				nodosSinProcesar.push(nodoHijo);
			}
		}
		return peso;
	}

	private ArrayList<Tabla> convertirColapsandoEnPadre(Entidad raiz) {
		ArrayList<Tabla> tablas = new ArrayList<Tabla>();

		Stack<Entidad> nodosSinProcesar = new Stack<Entidad>();
		nodosSinProcesar.push(raiz);

		while (!nodosSinProcesar.empty()) {
			Entidad nodoPadre = nodosSinProcesar.pop();
			for (Entidad nodoHijo : nodoPadre.getDerivadas()) {
				entidadesBorradas.add(nodoHijo);

				nodosSinProcesar.push(nodoHijo);
			}
		}
		Tabla tablaPadre = convertirEntidad(raiz);
		tablas.add(tablaPadre);

		nodosSinProcesar.push(raiz);
		while (!nodosSinProcesar.empty()) {
			Entidad nodoPadre = nodosSinProcesar.pop();
			for (Entidad nodoHijo : nodoPadre.getDerivadas()) {
				agregarAtributos(nodoHijo, tablaPadre, nodoHijo.getNombre() + "-");
				nodosSinProcesar.push(nodoHijo);
			}
		}

		return tablas;
	}

	private void agregarAtributosDePadre(Entidad padre, Tabla tablaHijo, Entidad hijo) {
		for (Atributo atributo : padre.getAtributos()) {
			if (atributo.getCardinalidadMaxima().equals("1")) {
				// monovalente
				if (atributo.getTipo() == TipoAtributo.CARACTERIZACION) {
					// solo se convierten los de caracterizacion
					if (!construirPK(padre, "").contains(atributo.getNombre()))
						agregarAtributo(atributo, tablaHijo, padre.getNombre() + "-");
				}
			} else {
				// polivalente
				Tabla tablaAtributo;

				tablaAtributo = new TablaControl(atributo.getNombre() + "_de_" + hijo.getNombre());

				agregarPK(padre, tablaAtributo, hijo.getNombre() + "-");

				agregarFKDePadre(padre, tablaAtributo, hijo.getNombre());

				if (atributo.esCompuesto()) {
					tablaAtributo.addClavePrimaria("id");
					agregarAtributo(atributo, tablaAtributo);

				} else {
					tablaAtributo.addClavePrimaria(atributo.getNombre());
				}
				diagramaLogico.agregar(tablaAtributo);
			}
		}

	}

	private ArrayList<Tabla> convertirColapsandoEnHijos(Entidad raiz) {
		ArrayList<Tabla> tablas = new ArrayList<Tabla>();
		entidadesBorradas.add(raiz);
		for (Entidad hijo : raiz.getDerivadas()) {
			Tabla tablaHijo = new TablaControl(hijo.getNombre());
			agregarPK(raiz, tablaHijo);

			agregarAtributosDePadre(raiz, tablaHijo, hijo);

			agregarAtributos(hijo, tablaHijo);
			agregarEntidadesRelacionadas(hijo, tablaHijo);
			tablas.add(tablaHijo);
			for (Relacion relacion : raiz.getRelaciones()) {
				relacionesDePadres.add(relacion);

			}

		}

		/*
		 * for (Relacion relacion : raiz.getRelaciones()) {
		 * relacionesProcesadas.add(relacion); }
		 */

		return tablas;
	}

	private ArrayList<Tabla> convertirSinColapsar(Entidad raiz) {
		ArrayList<Tabla> tablas = new ArrayList<Tabla>();
		tablas.add(convertirEntidad(raiz));

		Stack<Entidad> nodosSinProcesar = new Stack<Entidad>();
		nodosSinProcesar.push(raiz);

		while (!nodosSinProcesar.empty()) {
			Entidad nodoPadre = nodosSinProcesar.pop();
			for (Entidad nodoHijo : nodoPadre.getDerivadas()) {
				Tabla hijo=convertirEntidad(nodoHijo);
				hijo.addClaveForanea(construirPK(nodoPadre,""), nodoPadre.getNombre());
				tablas.add(hijo);
				nodosSinProcesar.push(nodoHijo);
			}
		}

		return tablas;
	}

}
