package mereditor.modelo.validacion;

import java.util.ArrayList;
import java.util.List;

import mereditor.modelo.base.ComponenteCardinal;

public class ValidarCardinalidadCompleta implements Validacion {

	@Override
	public List<Observacion> validar(Validable validable) {
		List<Observacion> observaciones = new ArrayList<>();

		ComponenteCardinal cardinal = (ComponenteCardinal) validable;
		
		if (cardinal.getCardinalidadMinima().isEmpty())
			observaciones.add(new Observacion("Cardinalidad Minima " + Observacion.NO_DEFINIDO));

		if (cardinal.getCardinalidadMaxima().isEmpty()) {
			observaciones.add(new Observacion("Cardinalidad Maxima " + Observacion.NO_DEFINIDO));
		}

		return observaciones;
	}

}
