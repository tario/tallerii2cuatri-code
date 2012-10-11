package mereditor.modelo.base;

import java.util.UUID;

import mereditor.modelo.validacion.ValidarNombreCompleto;

public abstract class ComponenteNombre extends Componente {
	
	protected String nombre = "";
	
	public ComponenteNombre() {
		super();
	}
	
	public ComponenteNombre(String nombre) {
		this(nombre, UUID.randomUUID().toString());
	}
	
	public ComponenteNombre(String nombre, String id) {
		super (id);
		this.nombre= nombre;
	}

	public ComponenteNombre(String nombre, String id, Componente padre) {
		super (id, padre);
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public void addValidaciones() {
		this.validaciones.add(new ValidarNombreCompleto());
		super.addValidaciones();
	}
	
	@Override
	public String toString() {
		return this.nombre;
	}
}
