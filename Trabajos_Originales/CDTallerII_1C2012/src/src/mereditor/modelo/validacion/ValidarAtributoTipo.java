package mereditor.modelo.validacion;

import java.util.ArrayList;
import java.util.List;

import mereditor.modelo.Atributo;
import mereditor.modelo.Atributo.TipoAtributo;

public class ValidarAtributoTipo implements Validacion {

	@Override
	public List<Observacion> validar(Validable componente) {
		List<Observacion> observaciones = new ArrayList<>();

		Atributo atributo = (Atributo) componente;

		TipoAtributo tipo = atributo.getTipo();

		if (tipo != null)
			if (tipo.equals(TipoAtributo.DERIVADO_CALCULO) && atributo.getFormula() == null)
				observaciones.add(new Observacion("Formula " + Observacion.NO_DEFINIDO));
			else if (tipo.equals(TipoAtributo.DERIVADO_COPIA) && atributo.getOriginal() == null)
				observaciones.add(new Observacion("Atributo Original " + Observacion.NO_DEFINIDO));

		return observaciones;
	}
}
