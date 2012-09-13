package mereditor.modelo.validacion;

import java.util.ArrayList;
import java.util.List;

import mereditor.modelo.Proyecto;
import mereditor.modelo.base.Componente;
import mereditor.modelo.base.ComponenteAtributos;

public class ValidarEquilibrioAtributos implements Validacion {

	@Override
	public List<Observacion> validar(Validable componente) {
		List<Observacion> observaciones = new ArrayList<>();

		Proyecto proyecto = (Proyecto) componente;

		// Inlcuir dentro de los componentes sólo a las entidades y las
		// relaciones.
		List<ComponenteAtributos> componentes = new ArrayList<>();
		componentes.addAll(proyecto.getEntidades());
		componentes.addAll(proyecto.getRelaciones());

		int total = 0;

		for (ComponenteAtributos componenteAtrs : componentes)
			total += componenteAtrs.getAtributos().size();

		int promedio = total / componentes.size();

		for (ComponenteAtributos componenteAtrs : componentes) {
			int delta = promedio - componenteAtrs.getAtributos().size();
			if (Math.abs(delta) > Validacion.MAX_DESVIACION_ATRIBUTOS) {
				String msj = "Tiene %d atributos mientras que el promedio es %d.";
				msj = String.format(msj, componenteAtrs.getAtributos().size(), promedio);
				observaciones.add(new Observacion((Componente) componenteAtrs, msj));
			}
		}

		return observaciones;
	}
}
