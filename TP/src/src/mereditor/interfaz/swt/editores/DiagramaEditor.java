package mereditor.interfaz.swt.editores;

import java.util.List;

import mereditor.modelo.Diagrama;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class DiagramaEditor extends Editor<Diagrama> {
	protected Text txtNombre;
	
	public DiagramaEditor(Diagrama diagrama) {
		super(diagrama);
		this.titulo = "Editor - " + componente.getNombre();
	}
	
	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);

		/**
		 * Campos generales.
		 */
		Composite header = new Composite(dialogArea, SWT.NONE);
		header.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		header.setLayout(new GridLayout(2, false));

		this.txtNombre = createLabelText(header, NOMBRE);
		this.txtNombre.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		return dialogArea;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(400, 120);
	}

	@Override
	protected void cargarDatos() {
		this.txtNombre.setText(this.componente.getNombre());		
	}

	@Override
	protected void aplicarCambios() {
		componente.setNombre(txtNombre.getText());		
	}

	@Override
	protected boolean validar(List<String> errors) {
		if(this.txtNombre.getText().length() == 0)
			errors.add("Debe completar el nombre.");

		return errors.isEmpty();
	}
}
