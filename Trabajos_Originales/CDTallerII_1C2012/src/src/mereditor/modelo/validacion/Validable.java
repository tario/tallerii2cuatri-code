package mereditor.modelo.validacion;

public interface Validable {
	/**
	 * Carga las validaciones para este componente.
	 */
	public void addValidaciones();
	/**
	 * Realiza la validacion.
	 * @return
	 */
	public Observacion validar();
}
