package mereditor.modelo.validacion;

import java.util.ArrayList;
import java.util.List;

import mereditor.modelo.base.ComponenteNombre;

import org.apache.commons.lang.StringUtils;

public class ValidarNombreCompleto implements Validacion {

	@Override
	public List<Observacion> validar(Validable componente) {
		List<Observacion> observaciones = new ArrayList<>();

		ComponenteNombre componenteNombre = (ComponenteNombre) componente;

		if (StringUtils.isEmpty(componenteNombre.getNombre()))
			observaciones.add(new Observacion("Nombre " + Observacion.NO_DEFINIDO));

		return observaciones;
	}
}
