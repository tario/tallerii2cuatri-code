package mereditor.interfaz.swt.builders;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DialogBuilder {

	public enum Resultado {
		OK, CANCEL
	}

	public static class PromptResult {
		public String value;
		public Resultado result = Resultado.CANCEL;
	}

	public static PromptResult prompt(Shell parent, String title, String message) {
		final Shell dialog = new Shell(parent, SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL);
		final PromptResult result = new PromptResult();

		dialog.setText(title);
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout(formLayout);

		Label label = new Label(dialog, SWT.NONE);
		label.setText(message);
		FormData data = new FormData();
		label.setLayoutData(data);

		Button cancel = new Button(dialog, SWT.PUSH);
		cancel.setText("Cancelar");
		data = new FormData();
		data.width = 60;
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		cancel.setLayoutData(data);
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				result.result = Resultado.CANCEL;
				dialog.close();
			}
		});

		final Text text = new Text(dialog, SWT.BORDER);
		data = new FormData();
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				result.value = text.getText();
			}
		});

		data.width = 200;
		data.left = new FormAttachment(label, 0, SWT.DEFAULT);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(label, 0, SWT.CENTER);
		data.bottom = new FormAttachment(cancel, 0, SWT.DEFAULT);
		text.setLayoutData(data);

		Button ok = new Button(dialog, SWT.PUSH);
		ok.setText("Aceptar");
		data = new FormData();
		data.width = 60;
		data.right = new FormAttachment(cancel, 0, SWT.DEFAULT);
		data.bottom = new FormAttachment(100, 0);
		ok.setLayoutData(data);
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				result.result = Resultado.OK;
				dialog.close();
			}
		});

		dialog.setDefaultButton(ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed()) {
			if (!dialog.getDisplay().readAndDispatch())
				dialog.getDisplay().sleep();
		}

		return result;
	}
}
