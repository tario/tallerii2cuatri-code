package mereditor.interfaz.swt.dialogs;

import java.util.Set;

import mereditor.control.EntidadControl;
import mereditor.interfaz.swt.editores.Editor;
import mereditor.interfaz.swt.editores.EditorFactory;
import mereditor.modelo.Entidad;

import org.eclipse.swt.widgets.Shell;

public class AgregarEntidadDialog extends AgregarComponenteDialog<Entidad> {

	/**
	 * @wbp.parser.constructor
	 */
	protected AgregarEntidadDialog(Shell shell) {
		super(shell);
	}

	public AgregarEntidadDialog() {
		super();
	}

	@Override
	protected Set<Entidad> loadComponentes() {
		// Obtener las entidades de los ancestros
		Set<Entidad> entidades = this.principal.getProyecto()
				.getEntidadesDisponibles();
		Set<Entidad> entidadesDiagrama = this.principal.getProyecto()
				.getEntidadesDiagrama();
		// Quitar las que ya tiene
		entidades.removeAll(entidadesDiagrama);

		return entidades;
	}
	
	@Override
	protected Editor<?> getEditor() {
		return EditorFactory.getEditor(new EntidadControl());
	}

	@Override
	protected String getNombreComponente() {
		return "Entidad";
	}


}
