package mereditor.interfaz.swt.dialogs;

import java.util.Set;

import mereditor.control.JerarquiaControl;
import mereditor.interfaz.swt.editores.Editor;
import mereditor.interfaz.swt.editores.EditorFactory;
import mereditor.modelo.Jerarquia;

import org.eclipse.swt.widgets.Shell;

public class AgregarJerarquiaDialog extends AgregarComponenteDialog<Jerarquia> {

	/**
	 * @wbp.parser.constructor
	 */
	protected AgregarJerarquiaDialog(Shell shell) {
		super(shell);
	}

	public AgregarJerarquiaDialog() {
		super();
	}

	@Override
	protected Set<Jerarquia> loadComponentes() {
		// Obtener las entidades de los ancestros
		Set<Jerarquia> relaciones = this.principal.getProyecto()
				.getJerarquiasDisponibles();
		Set<Jerarquia> entidadesDiagrama = this.principal.getProyecto()
				.getJerarquiasDiagrama();
		// Quitar las que ya tiene
		relaciones.removeAll(entidadesDiagrama);

		return relaciones;
	}
	
	@Override
	protected Editor<?> getEditor() {
		return EditorFactory.getEditor(new JerarquiaControl());
	}

	@Override
	protected String getNombreComponente() {
		return "Jerarquia";
	}


}
