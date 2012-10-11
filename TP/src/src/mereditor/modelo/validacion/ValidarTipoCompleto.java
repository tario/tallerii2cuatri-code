package mereditor.modelo.validacion;

import java.util.ArrayList;
import java.util.List;

import mereditor.modelo.base.ComponenteTipado;

public class ValidarTipoCompleto implements Validacion {

	@Override
	public List<Observacion> validar(Validable componente) {
		List<Observacion> observaciones = new ArrayList<>();

		ComponenteTipado<?> componenteNombre = (ComponenteTipado<?>) componente;

		if (componenteNombre.getTipo() == null)
			observaciones.add(new Observacion("Tipo " + Observacion.NO_DEFINIDO));

		return observaciones;
	}
}
