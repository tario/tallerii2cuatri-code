package mreleditor.xml;


import org.w3c.dom.Document;

import mereditor.modelo.Atributo;
import mereditor.modelo.Diagrama;
import mereditor.modelo.Entidad;
import mereditor.modelo.Entidad.Identificador;
import mereditor.modelo.base.Componente;
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
	//		agregarAtributos(entidad,tabla);
	//		agregarFK(entidad,tabla);
		}
		
	}
	
	private void agregarPK(Entidad entidad,Tabla tabla){
		for (Identificador identificador : entidad.getIdentificadores()) {
			if(identificador.isInterno()){
				// identificador de atributos
				agregarPKdeAtributos(identificador,tabla);
			}else if(identificador.isExterno()){
				// identificado por otras entidades
				agregarPKdeEntidades(identificador,tabla);
			}else{
				// identificado por entidades y atributos
				agregarPKdeAtributos(identificador,tabla);
				agregarPKdeEntidades(identificador,tabla);
			}
		}
	}
	private void agregarPKdeAtributos(Identificador identificador,Tabla tabla){
		for (Atributo atributoId: identificador.getAtributos()){
			if(!atributoId.esCompuesto())
				tabla.setClave_primaria(atributoId.getNombre());
			else{
				for(Atributo atributoIdcomponente: atributoId.getAtributos()){
					tabla.setClave_primaria(atributoIdcomponente.getNombre());
				}
			}
		}
	}
	private void agregarPKdeEntidades(Identificador identificador,Tabla tabla){
		for (Entidad entidadId: identificador.getEntidades()){
			agregarPK(entidadId,tabla);
		}
	}
	public Document convertir(Diagrama der){

		for (Componente componente : der.getComponentes()) {
			if (componente.es(Entidad.class) )
				convertirEntidad((Entidad)componente);
		}
		
		return null;

		
	}



}
