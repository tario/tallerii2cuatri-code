package mereditor.modelo.validacion;

import java.util.ArrayList;
import java.util.List;

import mereditor.modelo.Diagrama;
import mereditor.modelo.base.Componente;

import org.apache.commons.lang.StringUtils;

public class ValidarSuperposicion implements Validacion {

	@Override
	public List<Observacion> validar(Validable componente) {
		List<Observacion> observaciones = new ArrayList<>();

		Diagrama diagrama = (Diagrama) componente;

		for (Componente comp : diagrama.getComponentes()) {
			if (comp.getAllPadres().size() > 1) {
				String msj = "Pertenece a %d diagramas (%s).";
				String diagramas = StringUtils.join(comp.getAllPadres(), ", ");
				msj = String.format(msj, comp.getAllPadres().size(), diagramas);

				observaciones.add(new Observacion(comp, msj));
			}
		}

		return observaciones;
	}
}
