package mereditor.modelo;

public class Validacion {
	public enum EstadoValidacion {
		SIN_VALIDAR, VALIDADO, VALIDADO_CON_OBSERVACIONES
	}

	protected String observaciones;
	protected EstadoValidacion estado;

	public Validacion() {
		this.estado = EstadoValidacion.SIN_VALIDAR;
	}

	public Validacion(EstadoValidacion estado, String observaciones) {
		this.estado = estado;
		this.observaciones = observaciones;
	}

	public EstadoValidacion getEstado() {
		return this.estado;
	}

	public void setEstado(EstadoValidacion estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
