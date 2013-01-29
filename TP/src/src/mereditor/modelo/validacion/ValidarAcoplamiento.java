package mereditor.modelo.validacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mereditor.modelo.Diagrama;
import mereditor.modelo.Entidad;
import mereditor.modelo.Jerarquia;
import mereditor.modelo.Relacion;
import mereditor.modelo.Relacion.EntidadRelacion;

public class ValidarAcoplamiento implements Validacion {

	@Override
	public List<Observacion> validar(Validable componente) {
		List<Observacion> observaciones = new ArrayList<>();

		Diagrama diagrama = (Diagrama) componente;
		Set<Entidad> entidades = diagrama.getEntidades(false);
		Set<Relacion> relaciones = diagrama.getRelaciones(false);

		/*
		 * Verificar si alguna relacion tiene una entidad de otro diagrama.
		 */
		for (Relacion relacion : relaciones) {
			for (EntidadRelacion participante : relacion.getParticipantes()) {
				if (!entidades.contains(participante.getEntidad())) {
					String msj = "Tiene como participante a la entidad %s que no se encuentra incluida en este diagrama.";
					msj = String.format(msj, participante.getEntidad().getNombre());
					observaciones.add(new Observacion(relacion, msj));
				}
			}
		}

		/*
		 * Verificar si alguna entidad pertenece a una relacion de otro
		 * diagrama.
		 */
		for (Entidad entidad : entidades) {
			for (Relacion relacion : entidad.getRelaciones()) {
				if (!relaciones.contains(relacion)) {
					String msj = "Pertence a la relación %s que no se encuentra incluida en este diagrama.";
					msj = String.format(msj, relacion.getNombre(), relacion.getNombre());
					observaciones.add(new Observacion(entidad, msj));
				}
			}
		}

		for (Jerarquia jerarquia : diagrama.getJerarquias(false)) {
			if (!entidades.contains(jerarquia.getGenerica())) {
				String msj = "Tiene como genérica a la entidad %s que no se encuentra incluida en este diagrama.";
				msj = String.format(msj, jerarquia.getGenerica().getNombre());
				observaciones.add(new Observacion(jerarquia, msj));
			}

			for (Entidad entidad : jerarquia.getDerivadas()) {
				if (!entidades.contains(entidad)) {
					String msj = "La jerarquia %s tiene como derivada a la entidad %s que no se encunetra incluida en este diagrama.";
					msj = String.format(msj, jerarquia.toString(), entidad.getNombre());
					observaciones.add(new Observacion(jerarquia, msj));
				}
			}
		}

		return observaciones;
	}
}
