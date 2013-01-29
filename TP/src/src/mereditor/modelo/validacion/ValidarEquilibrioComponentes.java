package mereditor.modelo.validacion;

import java.util.ArrayList;
import java.util.List;

import mereditor.modelo.Diagrama;
import mereditor.modelo.Proyecto;

public class ValidarEquilibrioComponentes implements Validacion {

	@Override
	public List<Observacion> validar(Validable componente) {
		List<Observacion> observaciones = new ArrayList<>();

		Proyecto proyecto = (Proyecto) componente;

		int total = 0;
		for (Diagrama diagrama : proyecto.getDiagramas())
			total += diagrama.getComponentes().size();

		int promedio = total / proyecto.getDiagramas().size();

		for (Diagrama diagrama : proyecto.getDiagramas()) {
			int delta = promedio - diagrama.getComponentes().size();
			if (Math.abs(delta) > Validacion.MAX_DESVIACION_COMPONENTES) {
				String msj = "Tiene %d componentes mientras que el promedio es %d.";
				msj = String.format(msj, diagrama.getComponentes().size(), promedio);
				observaciones.add(new Observacion(diagrama, msj));
			}
		}

		return observaciones;
	}
}
