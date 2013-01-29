package mereditor.modelo.validacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mereditor.modelo.Diagrama;
import mereditor.modelo.base.Componente;
import mereditor.modelo.base.ComponenteAtributos;

public class ValidarClaridadAtributos implements Validacion {
	@Override
	public List<Observacion> validar(Validable componente) {
		List<Observacion> observaciones = new ArrayList<>();

		Diagrama diagrama = (Diagrama) componente;
		Set<ComponenteAtributos> componentes = Componente.filtrarComponentes(
				ComponenteAtributos.class, diagrama.getComponentes());

		for (ComponenteAtributos comp : componentes) {
			if (comp.getAtributos().size() > Validacion.MAX_ATRIBUTOS) {
				String msj = "Tiene más de %d atributos lo cual reduce su claridad.";
				msj = String.format(msj, Validacion.MAX_ATRIBUTOS);
				observaciones.add(new Observacion((Componente)comp, msj));
			}
		}

		return observaciones;
	}
}
