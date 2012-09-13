package mereditor.interfaz.swt.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import mereditor.modelo.base.Componente;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SeleccionarComponenteDialog<T extends Componente> extends Dialog {
	private Combo combo;

	private List<String> nombres = new ArrayList<>();
	private List<T> componentes;

	private T componente = null;	

	/**
	 * @wbp.parser.constructor
	 */
	protected SeleccionarComponenteDialog(Shell shell) {
		super(shell);
	}

	public SeleccionarComponenteDialog(Collection<T> componentes) {
		super();
		this.componentes = new ArrayList<>(componentes);
		this.titulo = "Seleccionar";
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		Label lblEntidades = new Label(container, SWT.LEFT);
		lblEntidades.setText("Seleccionar una opción:");

		this.combo = new Combo(container, SWT.READ_ONLY);
		this.combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.loadAtributos();

		return container;
	}

	/**
	 * Carga las entidades que pertenecen al diagrama actual y a sus padres en
	 * el combo.
	 */
	private void loadAtributos() {
		Collections.sort(this.componentes);

		for (T componente : this.componentes) {
			this.combo.add(componente.toString());
			this.nombres.add(componente.toString());
		}
	}
	
	/**
	 * Devuelve la entidad seleccionada o creada.
	 * @return
	 */
	public T getComponente() {
		return this.componente;
	}

	@Override
	protected void okPressed() {
		if (this.combo.getSelectionIndex() == -1) {
			this.principal.error("No seleccionó ninguna opción.");
		} else {
			String nombre = combo.getText();
			componente = this.componentes.get(this.nombres.indexOf(nombre));
			super.okPressed();
		}
	}
}
