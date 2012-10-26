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
	private void convertirEntidad(Entidad entidad){
		if(!entidad.esJerarquica()){
			// No forma una jerarquia
			Tabla tabla = new Tabla();
			agregarPK(entidad,tabla);
			agregarAtributos(entidad,tabla);
			agregarEntidadesRelacionadas(entidad,tabla);
		}
		
	}
	private void convertirRelacion(Relacion relacion){
		Tabla tabla=new Tabla();
		agregarPK(relacion,tabla);
		agregarFK(relacion,tabla);
		agregarAtributosDeRelacion(relacion,tabla);
		
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
				agregarPK(entidad,tablaAtributo);
				agregarFK(entidad,tablaAtributo);
				
				if(atributo.esCompuesto()){
					//tablaAtributo.addClave_primaria("id");
					agregarAtributo(atributo,tablaAtributo);
				}else{
					tablaAtributo.setClave_primaria(atributo.getNombre());	
				}
			}
		}
	}
	
	private void agregarAtributo(Atributo atributo,Tabla tabla){
		agregarAtributo(atributo,tabla,"");
	}
	private void agregarAtributo(Atributo atributo,Tabla tabla,String nombrePadre){
		if(atributo.esCompuesto()){
			for(Atributo atrComponente: atributo.getAtributos()){
				agregarAtributo(atrComponente, tabla,nombrePadre+"-"+atrComponente.getNombre());
			} 
		}else{
			//tabla.addAtributo(nombrePadre+"-"+atributo.getNombre());
		}
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
					agregarAtributosDeRelacion(relacion,tabla);
				}
				else if(entidadRelacion2.getEntidad().equals(entidad) && 
						entidadRelacion2.getCardinalidadMinima().equals("1") &&
						entidadRelacion2.getCardinalidadMaxima().equals("1")){
					
					agregarFK(entidadRelacion1.getEntidad(),tabla);
					agregarAtributosDeRelacion(relacion,tabla);
				}
			}
		}
	}
	
	private void agregarAtributosDeRelacion(Relacion relacion, Tabla tabla){
		for(Atributo atributo: relacion.getAtributos()){
			agregarAtributo(atributo,tabla,relacion.getNombre());
		}
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
	private void agregarPK(Entidad entidad,Tabla tabla){
		//tabla.addClave_primaria(construirPK(entidad));
	}
	private void agregarFK(Entidad entidad,Tabla tabla){
		//tabla.addClave_foranea(construirPK(entidad));
	}
	
	private ArrayList<String> construirPK(Entidad entidad){
		ArrayList<String> PK = new ArrayList<String>();
		
		for (Identificador identificador : entidad.getIdentificadores()) {
			if(identificador.isInterno()){
				// identificador de atributos
				PK=construirPKdeAtributos(identificador);
			}else if(identificador.isExterno()){
				// identificado por otras entidades
				PK=construirPKdeEntidades(identificador);
			}else{
				// identificado por entidades y atributos
				PK=construirPKdeAtributos(identificador);
				PK.addAll(construirPKdeEntidades(identificador));
			}
		}
		return PK;
	}

	private ArrayList<String> construirPKdeAtributos(Identificador identificador){
		ArrayList<String> PK = new ArrayList<String>();
		for (Atributo atributoId: identificador.getAtributos()){
			if(!atributoId.esCompuesto())
				PK.add(atributoId.getNombre());
			else{
				for(Atributo atributoIdcomponente: atributoId.getAtributos()){
					PK.add(atributoId.getNombre()+"-"+
							atributoIdcomponente.getNombre());
				}
			}
		}
		return PK;
	}
	private ArrayList<String> construirPKdeEntidades(Identificador identificador){
		ArrayList<String> PK = new ArrayList<String>();
		for (Entidad entidadId: identificador.getEntidades()){
			PK.addAll(construirPK(entidadId));
		}
		return PK;
	}
	
	
	public Document convertir(Diagrama der){

		for (Componente componente : der.getComponentes()) {
			if (componente.es(Entidad.class) )
				convertirEntidad((Entidad)componente);
		}
		
		return null;

		
	}



}
