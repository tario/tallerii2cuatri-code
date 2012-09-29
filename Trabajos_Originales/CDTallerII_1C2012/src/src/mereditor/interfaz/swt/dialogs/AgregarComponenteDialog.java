package mereditor.interfaz.swt.dialogs;

import java.util.Set;

import mereditor.interfaz.swt.editores.Editor;
import mereditor.modelo.base.Componente;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public abstract class AgregarComponenteDialog<T extends Componente> extends Dialog {
	private T componente = null;

	/**
	 * @wbp.parser.constructor
	 */
	protected AgregarComponenteDialog(Shell shell) {
		super(shell);
	}

	public AgregarComponenteDialog() {
		super();
		this.titulo = Editor.AGREGAR + " " + this.getNombreComponente();
	}

	protected abstract String getNombreComponente();

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		Button btnSeleccionar = new Button(container, SWT.PUSH);
		btnSeleccionar.setText("Seleccionar Existente");
		btnSeleccionar.addSelectionListener(this.seleccionar);
		btnSeleccionar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNueva = new Button(container, SWT.PUSH);
		btnNueva.setText(Editor.NUEVO);
		btnNueva.addSelectionListener(this.nuevo);
		btnNueva.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return container;
	}

	/**
	 * Carga las entidades que pertenecen al diagrama actual y a sus padres en
	 * el combo.
	 */
	protected abstract Set<T> loadComponentes();

	/**
	 * Devuelve la entidad seleccionada o creada.
	 * 
	 * @return
	 */
	public T getComponente() {
		return this.componente;
	}
	
	protected abstract Editor<?> getEditor();

	protected SelectionAdapter nuevo = new SelectionAdapter() {
		@SuppressWarnings("unchecked")
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			Editor<?> editor = getEditor();
			int result = editor.open();
			componente = (T) editor.getComponente();
			setReturnCode(result);
			close();
		};
	};

	protected SelectionAdapter seleccionar = new SelectionAdapter() {
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			SeleccionarComponenteDialog<T> dialog = new SeleccionarComponenteDialog<T>(
					loadComponentes());
			int result = dialog.open();
			componente = (T)dialog.getComponente();
			setReturnCode(result);
			close();
		};
	};
}
