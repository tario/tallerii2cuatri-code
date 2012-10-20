package mreleditor.xml;

import org.w3c.dom.Document;

import mereditor.modelo.Diagrama;

public class ConversorDERaLogico {
	
	private static ConversorDERaLogico instance=null;
	
	private ConversorDERaLogico(){}
	
	public static ConversorDERaLogico getInstance(){
		if(instance==null)
			instance=new ConversorDERaLogico();
		return instance;
	}
	public Document convertir(Diagrama der){
		return null;
	}

}
