package mreleditor.conversor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

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


public class ConversorDERaLogico{
	
	private static ConversorDERaLogico instance=null;
	
	private ConversorDERaLogico(){
		inicializarVariables();
		
	}
	private void inicializarVariables(){
		der=null;
		raicesProcesadas=new HashSet<Entidad>();
		tipoDeJerarquia= new HashMap<Entidad,Jerarquia.TipoJerarquia>();
		entidadPadre= new HashMap<Entidad,Entidad>();
		relacionesProcesadas= new HashSet<Relacion>();
	}
	
	public static ConversorDERaLogico getInstance(){
		if(instance==null)
			instance=new ConversorDERaLogico();
		return instance;
	}
	
	private Diagrama der;
	private enum TipoConversionDeJerarquia{COLAPSAR_EN_PADRE,COLAPSAR_EN_HIJOS,SIN_COLAPSAR};
	private Set<Entidad> raicesProcesadas;
	private HashMap<Entidad,Jerarquia.TipoJerarquia> tipoDeJerarquia;
	private HashMap<Entidad,Entidad> entidadPadre;
	private Set<Relacion> relacionesProcesadas;
	
	public DiagramaLogico convertir(Diagrama der){
		inicializarVariables();
		DiagramaLogico diagramaLogico=new DiagramaLogico();
		this.der=der;
		
		// Primero convierte las jerarquias
		for(Entidad raiz: getRaices()){
			for(Tabla tablaDeJerarquia:convertirJerarquia(raiz)){
					diagramaLogico.agregar(tablaDeJerarquia);
			}
		}
		for (Componente componente : der.getComponentes()) {
			if (componente.es(Entidad.class) ){
				if(!((Entidad)componente).esJerarquica()){
					// No forma una jerarquia
					Tabla tablaEntidad= convertirEntidad((Entidad)componente);
						diagramaLogico.agregar(tablaEntidad);
				}
			}
			else if(componente.es(Relacion.class)){
				if(! relacionesProcesadas.contains((Relacion)componente)){
					Tabla tabla=convertirRelacion((Relacion) componente);
						diagramaLogico.agregar(tabla);
				}
			}
		}
		
		return diagramaLogico;
		
	}
	
	/*						
	 *   Interfaz privada	
	 *   					
	 */						
	
	private Tabla convertirEntidad(Entidad entidad){
		Tabla tabla = new Tabla(entidad.getNombre());
			
			agregarPK(entidad,tabla);
			agregarAtributos(entidad,tabla);
			agregarEntidadesRelacionadas(entidad,tabla);
		
		return tabla;
	}
	private void agregarPK(Entidad entidad,Tabla tabla){
		agregarPK(entidad,tabla,"");
	}
	private void agregarPK(Entidad entidad,Tabla tabla,String prefijo){
		Entidad padre=entidadPadre.get(entidad);
		if(padre==null)
			padre=entidad;
		tabla.addClavePrimaria(construirPK(entidad,prefijo));
	}
	
	private ArrayList<String> construirPK(Entidad entidad,String prefijo){
		ArrayList<String> PK = new ArrayList<String>();
		
		for (Identificador identificador : entidad.getIdentificadores()) {
			if(identificador.isInterno()){
				// identificador de atributos
				PK=construirPKdeAtributos(identificador,prefijo);
			}else if(identificador.isExterno()){
				// identificado por otras entidades
				PK=construirPKdeEntidades(identificador,prefijo);
			}else{
				// identificado por entidades y atributos
				PK=construirPKdeAtributos(identificador,prefijo);
				PK.addAll(construirPKdeEntidades(identificador,prefijo));
			}
		}
		return PK;
	}
	
	private ArrayList<String> construirPKdeAtributos(Identificador identificador, String prefijo){
		ArrayList<String> PK = new ArrayList<String>();
		for (Atributo atributoId: identificador.getAtributos()){
			if(atributoId.getCardinalidadMaxima().equals("1")){
				// monovalente
				PK.addAll(construirAtributos(atributoId,prefijo));
			}
	
		}
		return PK;
	}
	private ArrayList<String> construirPKdeEntidades(Identificador identificador, String prefijo){
		ArrayList<String> PK = new ArrayList<String>();
		for (Entidad entidadId: identificador.getEntidades()){
			PK.addAll(construirPK(entidadId,prefijo+entidadId.getNombre()+"-"));
		}
		return PK;
	}
	
	/*
	 *  Construye una lista de atributos componentes del atributoPadre
	 */
	private ArrayList<String> construirAtributos(Atributo atributoPadre){
		return construirAtributos(atributoPadre ,"");
	}
	private ArrayList<String> construirAtributos(Atributo atributoPadre , String prefijo){
		ArrayList<String> atributos = new ArrayList<String>();
		if(atributoPadre.getCardinalidadMaxima().equals("1")){
			// monovalente
		
			if( ! atributoPadre.esCompuesto() )
				atributos.add(prefijo+atributoPadre.getNombre());
			else{
				for(Atributo atributoComponente: atributoPadre.getAtributos()){
					atributos.addAll(construirAtributos(atributoComponente, atributoPadre.getNombre()+"-"));
				}
			}
		}
		return atributos;
	}
	private void agregarAtributos(Entidad entidad,Tabla tabla){
		agregarAtributos( entidad, tabla,"");
	}
	private void agregarAtributos(Entidad entidad,Tabla tabla,String prefijo){
		for(Atributo atributo : entidad.getAtributos()){
			if(atributo.getCardinalidadMaxima().equals("1")){
				// monovalente
				if(atributo.getTipo()==TipoAtributo.CARACTERIZACION){
					// solo se convierten los de caracterizacion
					agregarAtributo(atributo,tabla,prefijo);
				}
			}else{
				// polivalente
				Tabla tablaAtributo=new Tabla(atributo.getNombre()+"_de_"+entidad.getNombre());
				agregarPK(entidad,tablaAtributo,entidad.getNombre()+"-");
				agregarFK(entidad,tablaAtributo,entidad.getNombre()+"-");
				
				if(atributo.esCompuesto()){
					tablaAtributo.addClavePrimaria("id");
					agregarAtributo(atributo,tablaAtributo);
				}else{
					tablaAtributo.addClavePrimaria(atributo.getNombre());	
				}
			}
		}
	}
	private void agregarAtributo(Atributo atributo,Tabla tabla){
		agregarAtributo(atributo,tabla,"");
	}
	private void agregarAtributo(Atributo atributo,Tabla tabla,String prefijo){
		for(String atributoComponente: construirAtributos(atributo, prefijo)){
			tabla.addAtributo(atributoComponente);
		}
	}
	private int getCantidadAtributosMonovalentes(Entidad entidad){
		int cant=0;
	
		for(Atributo atributo : entidad.getAtributos()){
			if(atributo.getCardinalidadMaxima().equals("1")){
				// monovalente
				if(atributo.getTipo()==TipoAtributo.CARACTERIZACION){
					cant+=construirAtributos(atributo).size();
				}
			}
		}
		return cant;
	}
	private void agregarFK(Entidad entidad,Tabla tabla){
		agregarFK(entidad,tabla,"");
	}
	private Set<Entidad> entidadesBorradas;
	private void agregarFK(Entidad entidad,Tabla tabla,String prefijo){
		String nombreTabla=entidad.getNombre();
		Entidad padre=entidadPadre.get(entidad);
		boolean borrada=entidadesBorradas.contains(entidad);
		if( borrada )
			nombreTabla=padre.getNombre();
			tabla.addClaveForanea(construirPK(entidad,entidad.getNombre()+"-"),nombreTabla);
	}
	

	
	private void agregarEntidadesRelacionadas(Entidad entidad, Tabla tabla){

		for(Relacion relacion: entidad.getRelaciones()){
			if(relacion.getParticipantes().size()==2){
				Iterator<EntidadRelacion> it= relacion.getParticipantes().iterator();
				EntidadRelacion entidadRelacion1 = it.next();
				EntidadRelacion entidadRelacion2 = it.next();
				
				if(entidadRelacion1.getEntidad().equals(entidad) && 
					entidadRelacion1.getCardinalidadMinima().equals("1") &&
					entidadRelacion1.getCardinalidadMaxima().equals("1")){
					
					agregarFK(entidadRelacion2.getEntidad(),tabla);
					agregarAtributos(relacion,tabla,relacion.getNombre()+"-");
				}
				else if(entidadRelacion2.getEntidad().equals(entidad) && 
						entidadRelacion2.getCardinalidadMinima().equals("1") &&
						entidadRelacion2.getCardinalidadMaxima().equals("1")){
					
					agregarFK(entidadRelacion1.getEntidad(),tabla);
					agregarAtributos(relacion,tabla,relacion.getNombre()+"-");
				}
			}
		}
	}
	private void agregarAtributos(Relacion relacion, Tabla tabla){
		agregarAtributos(relacion, tabla,"");
	}
	private void agregarAtributos(Relacion relacion, Tabla tabla,String prefijo){
		for(Atributo atributo: relacion.getAtributos()){
			agregarAtributo(atributo,tabla,prefijo);
		}
	}
	
	
	private Tabla convertirRelacion(Relacion relacion){
		Tabla tabla=new Tabla();
		agregarPK(relacion,tabla);
		agregarFK(relacion,tabla);
		agregarAtributos(relacion,tabla);
		return tabla;
		
	}
	private Tabla convertirRelacion(Relacion relacion, Entidad entidadHijo){
		Tabla tabla=new Tabla();
		agregarPK(relacion,tabla);
		agregarFK(relacion,tabla,entidadHijo);
		agregarAtributos(relacion,tabla);
		return tabla;
		
	}
	
	private void agregarPK(Relacion relacion,Tabla tabla){
		boolean PKagregada=false;
		EntidadRelacion ultimaEntidadRelacion=null;
		for(EntidadRelacion entidadRelacion: relacion.getParticipantes()){
			ultimaEntidadRelacion=entidadRelacion;
			String cardinalidad=entidadRelacion.getCardinalidadMinima()+
					entidadRelacion.getCardinalidadMaxima();	
			if(!cardinalidad.equals("01") && !cardinalidad.equals("11")){
				agregarPK(entidadRelacion.getEntidad(),tabla);	
				PKagregada=true;
			}
		}
		if(PKagregada==false)
			agregarPK(ultimaEntidadRelacion.getEntidad(),tabla);	
			
	}
	private void agregarFK(Relacion relacion,Tabla tabla){
		for(EntidadRelacion entidadRelacion: relacion.getParticipantes()){
			agregarFK(entidadRelacion.getEntidad(),tabla);	
		}
	}
	private void agregarFK(Relacion relacion,Tabla tabla, Entidad entidadHijo){
		Entidad padre= entidadPadre.get(entidadHijo);
		for(EntidadRelacion entidadRelacion: relacion.getParticipantes()){
			if(entidadRelacion.getEntidad().equals(padre))
				agregarFK(entidadHijo,tabla);	
			else
				agregarFK(entidadRelacion.getEntidad(),tabla);	
		}
	}
	
	
	
	private ArrayList<Entidad> getRaices(){
		ArrayList<Entidad> raices= new ArrayList<Entidad>();
		for (Jerarquia jerarquia : der.getJerarquias(true)) {	
			if(raicesProcesadas.contains(jerarquia.getRaiz()))
				continue;
			procesarRaiz(jerarquia);
			raices.add(jerarquia.getRaiz());
		}
		return raices;
		
	}
	private ArrayList<Tabla> convertirJerarquia(Entidad raiz){
		ArrayList<Tabla> tablas=new ArrayList<Tabla>();
		switch(getTipoDeConversion(raiz)){
		case COLAPSAR_EN_PADRE:
				tablas=convertirColapsandoEnPadre(raiz);
				break;
		case COLAPSAR_EN_HIJOS: 
			tablas=convertirColapsandoEnHijos(raiz);
				break;
		case SIN_COLAPSAR: 
			tablas=convertirSinColapsar(raiz);
				break;
		}
		return tablas;
	}
	
	
	private void procesarRaiz(Jerarquia jerarquia){

		Entidad raiz=jerarquia.getRaiz();
		Stack<Entidad> nodosSinProcesar= new Stack<Entidad>();
		nodosSinProcesar.push(raiz);
		
		while(!nodosSinProcesar.empty()){
			Entidad nodoPadre=nodosSinProcesar.pop();
			for(Entidad nodoHijo:nodoPadre.getDerivadas()){
				entidadPadre.put(nodoHijo, nodoPadre );
				nodosSinProcesar.push(nodoHijo);
			}	
		}
		raicesProcesadas.add(raiz);
		if(raiz.getNivel()==1)
			tipoDeJerarquia.put(raiz,jerarquia.getTipo());

	}
	
	private TipoConversionDeJerarquia getTipoDeConversion(Entidad raiz){
		int pesoDePadres=calcularPesoPadres(raiz);
		int pesoDeHijos=calcularPesoHijos(raiz);
		
		if(pesoDePadres > 10 && pesoDeHijos > 10)
			return TipoConversionDeJerarquia.SIN_COLAPSAR;
		if (pesoDePadres < pesoDeHijos)
			return TipoConversionDeJerarquia.COLAPSAR_EN_HIJOS;
		
		return TipoConversionDeJerarquia.COLAPSAR_EN_PADRE;
		
	}
	
	private int calcularPesoPadres(Entidad raiz){
		Jerarquia.TipoJerarquia tipo= tipoDeJerarquia.get(raiz);
		if(tipo == null || tipo != Jerarquia.TipoJerarquia.TOTAL_EXCLUSIVA)
			return Integer.MAX_VALUE;
		int peso=getCantidadAtributosMonovalentes(raiz);
		peso+= 4 * raiz.getRelaciones().size();
		return peso;
	}
	private int calcularPesoHijos(Entidad raiz){
		int peso=0;
		Stack<Entidad> nodosSinProcesar= new Stack<Entidad>();
		nodosSinProcesar.push(raiz);
		
		while(!nodosSinProcesar.empty()){
			Entidad nodoPadre=nodosSinProcesar.pop();
			for(Entidad nodoHijo:nodoPadre.getDerivadas()){
				peso+=getCantidadAtributosMonovalentes(nodoHijo);
				peso+= 2 * nodoHijo.getRelaciones().size();
				nodosSinProcesar.push(nodoHijo);
			}	
		}
		return peso;
	}
	
	private ArrayList<Tabla> convertirColapsandoEnPadre(Entidad raiz){
		ArrayList<Tabla> tablas= new ArrayList<Tabla>();
		Tabla tablaPadre=convertirEntidad(raiz);
		tablas.add(tablaPadre);
		Stack<Entidad> nodosSinProcesar= new Stack<Entidad>();
		nodosSinProcesar.push(raiz);
		
		while(!nodosSinProcesar.empty()){
			Entidad nodoPadre=nodosSinProcesar.pop();
			for(Entidad nodoHijo:nodoPadre.getDerivadas()){
				entidadesBorradas.add(nodoHijo);
				agregarAtributos(nodoHijo, tablaPadre,nodoHijo.getNombre()+"-");
				nodosSinProcesar.push(nodoHijo);
			}	
		}
		return tablas;
	}
	private ArrayList<Tabla> convertirColapsandoEnHijos(Entidad raiz){
		ArrayList<Tabla> tablas = new ArrayList<Tabla>();
		for(Entidad hijo: raiz.getDerivadas()){
			Tabla tablaHijo= new Tabla(hijo.getNombre());
			agregarPK(raiz,tablaHijo);
			agregarAtributos(raiz, tablaHijo, raiz.getNombre()+"-");
			agregarAtributos(hijo, tablaHijo);
			agregarEntidadesRelacionadas(hijo, tablaHijo);
			tablas.add(tablaHijo);
			for(Relacion relacion : raiz.getRelaciones()){
				tablas.add(convertirRelacion(relacion,hijo));
			}
		}
		for(Relacion relacion : raiz.getRelaciones()){
			relacionesProcesadas.add(relacion);
		}
			
		return tablas;
	}
	private ArrayList<Tabla> convertirSinColapsar(Entidad raiz){
		ArrayList<Tabla> tablas = new ArrayList<Tabla>();
		tablas.add(convertirEntidad(raiz));
		
		Stack<Entidad> nodosSinProcesar= new Stack<Entidad>();
		nodosSinProcesar.push(raiz);
		
		while(!nodosSinProcesar.empty()){
			Entidad nodoPadre=nodosSinProcesar.pop();
			for(Entidad nodoHijo:nodoPadre.getDerivadas()){
				tablas.add(convertirEntidad(nodoHijo));
				nodosSinProcesar.push(nodoHijo);
			}	
		}
		
		return tablas;
	}

}
