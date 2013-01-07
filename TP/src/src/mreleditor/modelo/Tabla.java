/**
 * 
 */
package mreleditor.modelo;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


import mereditor.modelo.base.ComponenteNombre;
/**
 * @author guido
 *
 */
public class Tabla extends ComponenteNombre {

	protected Set<String> clavePrimaria=new HashSet<String>();
	protected Set<ClaveForanea> clavesForaneas=new HashSet<ClaveForanea>();
	protected Set<String> atributos=new HashSet<String>();
	
	public Tabla() {
		
	}
	
	public Tabla(Tabla tab) {
		this.setAtributos(tab.getAtributos());
		this.setClavePrimaria(tab.getClavePrimaria());
		this.setNombre(tab.getNombre());
		this.setPadre(tab.getPadre());
		this.clavesForaneas.addAll(tab.getClavesForaneas());
		this.id = tab.id;
		this.validaciones.addAll(tab.validaciones);
	}
	
	public Tabla (String nombre) {
		super(nombre);
	}
	
	public Set<String> getClavePrimaria() {
		return clavePrimaria;
	}
	public void setClavePrimaria(Collection<String> clavePrimaria) {
		this.clavePrimaria.addAll( clavePrimaria);
	}
	public Set<ClaveForanea> getClavesForaneas() {
		return clavesForaneas;
	}
	
	public Set<String> getAtributos() {
		return atributos;
	}
	public void setAtributos(Collection <String> atributos) {
		this.atributos .addAll( atributos);
	}
	
	public void addAtributo(String atributo) {
		this.atributos.add(atributo);
	}
	
	public void addClaveForanea(String fk, String nombreTabla) {
		
		atributos.add(fk);
		Set<String> fkSet=new HashSet<String>();
		fkSet.add(fk);
		clavesForaneas.add(new ClaveForanea(fkSet,nombreTabla));
	}
	
	public void addClaveForanea(Collection<String> fks, String nombreTabla) {
		for(String atributo:fks){
			atributos.add(atributo);
		}
		clavesForaneas.add(new ClaveForanea(fks,nombreTabla));
	}
	
	public void addClavePrimaria(Collection<String> pks) {
		this.clavePrimaria.addAll(pks);
		atributos.addAll(pks);
		
	}
	
	public void addClavePrimaria(String pk) {
		this.clavePrimaria.add(pk);
		atributos.add(pk);
	}
	@Override
	public boolean equals(Object object){
		Tabla tabla=(Tabla)object;
		if(!nombre.equals(tabla.nombre))
			return false;
		if(atributos.size()!= tabla.atributos.size())
			return false;
		for(String atributo:atributos){
			if(!tabla.atributos.contains(atributo))
				return false;
		}
		if(clavePrimaria.size()!= tabla.clavePrimaria.size())
			return false;
		for(String clave:clavePrimaria){
			if(!tabla.clavePrimaria.contains(clave))
				return false;
		}
		if(clavesForaneas.size()!= tabla.clavesForaneas.size())
			return false;
		for(ClaveForanea foranea:clavesForaneas){
			if(!(tabla.clavesForaneas.contains(foranea)))
				return false;
		}
		
		return true;
	}
	public class ClaveForanea{
		Set<String> atributos;
		

		String tablaReferenciada;
		
		public ClaveForanea(){
			atributos=new HashSet<String>();
		}
		public ClaveForanea(Collection<String> atributos,String tablaReferenciada){
			this.atributos=new HashSet<String>();
			this.atributos.addAll(atributos);
			this.tablaReferenciada=tablaReferenciada;
		}
		public Set<String> getAtributos() {
			return atributos;
		}

		public void setAtributos(Collection<String> atributos) {
			this.atributos.addAll(atributos);
		}
		public String getTablaReferenciada() {
			return tablaReferenciada;
		}
		public void setTablaReferenciada(String tablaReferenciada) {
			this.tablaReferenciada = tablaReferenciada;
		}
		@Override
		public boolean equals(Object object){
			ClaveForanea foranea=(ClaveForanea)object;
			if(!tablaReferenciada.equals(foranea.tablaReferenciada))
				return false;
			if(atributos.size()!=foranea.atributos.size())
				return false;
			for(String atributo:atributos){
				if(!foranea.atributos.contains(atributo))
					return false;
			}
			return true;
		}
		 @Override
		    public int hashCode() {
			 	String strCode=tablaReferenciada;
			 	int code=0;
				for(String atributo:atributos){
					strCode=strCode+atributo;
				}
				byte[] bytes=strCode.getBytes();
				for(int i=0;i<bytes.length;i++){
					code+=(int)bytes[i];
				}
				code*=strCode.length();
		        return code;
		    }
	}
	
		
}
