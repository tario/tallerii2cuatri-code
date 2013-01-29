package mereditor.interfaz.swt.editores;

import mereditor.modelo.Atributo;
import mereditor.modelo.Diagrama;
import mereditor.modelo.Entidad;
import mereditor.modelo.Jerarquia;
import mereditor.modelo.Relacion;
import mereditor.modelo.base.Componente;

public class EditorFactory {

	public static Editor<?> getEditor(Componente componente) {
		if (Diagrama.class.isInstance(componente))
			return new DiagramaEditor((Diagrama) componente);

		if (Entidad.class.isInstance(componente))
			return new EntidadEditor((Entidad) componente);

		if (Relacion.class.isInstance(componente))
			return new RelacionEditor((Relacion) componente);
		
		if (Atributo.class.isInstance(componente))
			return new AtributoEditor((Atributo) componente);

		if (Jerarquia.class.isInstance(componente))
			return new JerarquiaEditor((Jerarquia) componente);
		

		throw new RuntimeException("No existe un editor para el componente de tipo " + componente.getClass().getName());
	}
}
