package mereditor.interfaz.swt.examples;

import mereditor.interfaz.swt.Principal;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class EdicionDialog extends ApplicationWindow {

	public EdicionDialog(Principal principal) {
		super(principal.getShell());
	}
	
	@Override
	protected Control createContents(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		
		parent.setLayout(layout);
		
		Button btnAceptar = new Button(parent, SWT.PUSH);
		btnAceptar.setText("Aceptar");
		
		return super.createContents(parent);		
	}
}
