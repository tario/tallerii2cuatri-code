package mreleditor.xml;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;

import mereditor.modelo.Atributo;
import mereditor.modelo.Atributo.TipoAtributo;
import mereditor.modelo.Diagrama;
import mereditor.modelo.Entidad;
import mereditor.modelo.Entidad.Identificador;
import mereditor.modelo.Relacion;
import mereditor.modelo.Relacion.EntidadRelacion;
import mereditor.modelo.base.Componente;
import mereditor.modelo.base.ComponenteAtributos;
import mreleditor.modelo.Tabla;


public class ConversorDERaLogico{
	
	private static ConversorDERaLogico instance=null;
	
	private ConversorDERaLogico(){}
	
	public static ConversorDERaLogico getInstance(){
		if(instance==null)
			instance=new ConversorDERaLogico();
		return instance;
	}
	
	
	public Diagrama convertir(Diagrama der){
		//DiagramaLogico diagramaLogico=new DiagramaLogico();
		for (Componente componente : der.getComponentes()) {
			if (componente.es(Entidad.class) )
					for(Tabla tablaDeJerarquia:convertirEntidad((Entidad)componente))
						//diagramaLogico.agregar(tablaDeJerarquia);
						;
				
			else if(componente.es(Relacion.class)){
				Tabla tabla=convertirRelacion((Relacion) componente);
				//diagramaLogico.agregar(tabla);
			}
		}
		
		return null;
		
	}
	
	/*						
	 *   Interfaz privada	
	 *   					
	 */						
	
	private ArrayList<Tabla> convertirEntidad(Entidad entidad){
		ArrayList<Tabla> tablas= new ArrayList<Tabla>();
		if(!entidad.esJerarquica()){
			// No forma una jerarquia
			Tabla tabla = new Tabla();
			agregarPK(entidad,tabla);
			agregarAtributos(entidad,tabla);
			agregarEntidadesRelacionadas(entidad,tabla);
			tablas.add(tabla);
		}
		else{
			tablas=convertirEntidadJerarquica();
		}
		return tablas;
	}
	private void agregarPK(Entidad entidad,Tabla tabla){
		agregarPK(entidad,tabla,"");
	}
	private void agregarPK(Entidad entidad,Tabla tabla,String prefijo){
		//tabla.addClave_primaria(construirPK(entidad,prefijo));
	}
	private ArrayList<String> construirPK(Entidad entidad){
		return construirPK(entidad,"");
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
	private ArrayList<String> construirPKdeAtributos(Identificador identificador){
		return construirPKdeAtributos(identificador,"");
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
	private ArrayList<String> construirPKdeEntidades(Identificador identificador){
		return construirPKdeEntidades(identificador,"");
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
		for(Atributo atributo : entidad.getAtributos()){
			if(atributo.getCardinalidadMaxima().equals("1")){
				// monovalente
				if(atributo.getTipo()==TipoAtributo.CARACTERIZACION){
					// solo se convierten los de caracterizacion
					agregarAtributo(atributo,tabla);
				}
			}else{
				// polivalente
				Tabla tablaAtributo=new Tabla(/*atributo.getNombre()+"_de_"+entidad.getNombre()*/);
				agregarPK(entidad,tablaAtributo,entidad.getNombre()+"-");
				agregarFK(entidad,tablaAtributo,entidad.getNombre()+"-");
				
				if(atributo.esCompuesto()){
					//tablaAtributo.addClave_primaria("id");
					agregarAtributo(atributo,tablaAtributo);
				}else{
					//tablaAtributo.setClave_primaria(atributo.getNombre());	
				}
			}
		}
	}
	private void agregarAtributo(Atributo atributo,Tabla tabla){
		agregarAtributo(atributo,tabla,"");
	}
	private void agregarAtributo(Atributo atributo,Tabla tabla,String prefijo){
		for(String atributoComponente: construirAtributos(atributo, prefijo)){
			tabla.agregarAtributo(atributoComponente);
		}
	}
	private void agregarFK(Entidad entidad,Tabla tabla){
		agregarFK(entidad,tabla,"");
	}
	private void agregarFK(Entidad entidad,Tabla tabla,String prefijo){
		//tabla.addClave_foranea(construirPK(entidad,entidad.getNombre()+"-"),entidad.getNombre());
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
	
	private void agregarPK(Relacion relacion,Tabla tabla){
		for(EntidadRelacion entidadRelacion: relacion.getParticipantes()){
			String cardinalidad=entidadRelacion.getCardinalidadMinima()+
					entidadRelacion.getCardinalidadMaxima();	
			if(!cardinalidad.equals("01") && !cardinalidad.equals("11")){
				agregarPK(entidadRelacion.getEntidad(),tabla);	
			}
		}
	}
	private void agregarFK(Relacion relacion,Tabla tabla){
		for(EntidadRelacion entidadRelacion: relacion.getParticipantes()){
			agregarFK(entidadRelacion.getEntidad(),tabla);	
		}
	}
	
	private ArrayList<Tabla> convertirEntidadJerarquica(){
		
		// TODO: implemetar conversion de jerarquias
		return null;
		
	}
	
	



}
