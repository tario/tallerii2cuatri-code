package mereditor.interfaz.swt.dialogs;

import java.util.Set;

import mereditor.control.RelacionControl;
import mereditor.interfaz.swt.editores.Editor;
import mereditor.interfaz.swt.editores.EditorFactory;
import mereditor.modelo.Relacion;

import org.eclipse.swt.widgets.Shell;

public class AgregarRelacionDialog extends AgregarComponenteDialog<Relacion> {

	/**
	 * @wbp.parser.constructor
	 */
	protected AgregarRelacionDialog(Shell shell) {
		super(shell);
	}

	public AgregarRelacionDialog() {
		super();
	}

	@Override
	protected Set<Relacion> loadComponentes() {
		// Obtener las entidades de los ancestros
		Set<Relacion> relaciones = this.principal.getProyecto().getRelacionesDisponibles();
		Set<Relacion> entidadesDiagrama = this.principal.getProyecto().getRelacionesDiagrama();
		// Quitar las que ya tiene
		relaciones.removeAll(entidadesDiagrama);

		return relaciones;
	}

	@Override
	protected Editor<?> getEditor() {
		return EditorFactory.getEditor(new RelacionControl());
	}

	@Override
	protected String getNombreComponente() {
		return "Relacion";
	}
}
