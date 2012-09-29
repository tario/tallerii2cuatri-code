package mereditor.interfaz.swt.editores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mereditor.control.AtributoControl;
import mereditor.modelo.Atributo;
import mereditor.modelo.Atributo.TipoAtributo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AtributoEditor extends Editor<Atributo> {
	protected Text txtNombre;
	protected Combo cboTipo;
	protected Text txtCardinalidadMin;
	protected Text txtCardinalidadMax;
	protected Text txtFormula;
	protected Combo cboOriginal;

	protected AtributosTabla tblAtributos;
	protected List<Atributo> atributosOptions;

	/**
	 * Constructor para el editor visual
	 * 
	 * @wbp.parser.constructor
	 */
	protected AtributoEditor(Shell shell) {
		super(shell);
		this.componente = new AtributoControl();
	}

	public AtributoEditor(Atributo atributo) {
		super(atributo);
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

		this.txtNombre = createLabelText(header, Editor.NOMBRE);
		this.txtNombre.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		this.cboTipo = createLabelCombo(header, Editor.TIPO);
		this.cboTipo.setItems(Editor.TiposAtributo);
		this.cboTipo.addSelectionListener(this.selectedTipo);

		this.txtCardinalidadMin = createLabelText(header, Editor.CARDINALIDAD_MIN);
		GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gridData.widthHint = 15;
		this.txtCardinalidadMin.setLayoutData(gridData);

		this.txtCardinalidadMax = createLabelText(header, Editor.CARDINALIDAD_MAX);
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gridData.widthHint = 15;
		this.txtCardinalidadMax.setLayoutData(gridData);

		this.cboOriginal = createLabelCombo(header, Editor.ORIGINAL);
		this.cboOriginal.setItems(this.getAtributosDisponibles());

		this.txtFormula = createLabelText(header, Editor.FORMULA);
		this.txtFormula.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		/**
		 * Atributos.
		 */
		Group grupoAtributos = new Group(dialogArea, SWT.NONE);
		grupoAtributos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		grupoAtributos.setText(Editor.ATRIBUTOS);
		grupoAtributos.setLayout(new GridLayout(1, true));

		Composite botonesAtributos = new Composite(grupoAtributos, SWT.NONE);
		botonesAtributos.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		botonesAtributos.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button btnNuevoAtributo = new Button(botonesAtributos, SWT.PUSH);
		btnNuevoAtributo.setText(Editor.NUEVO);

		Button btnEliminarAtributo = new Button(botonesAtributos, SWT.PUSH);
		btnEliminarAtributo.setText(Editor.ELIMINAR);

		this.tblAtributos = new AtributosTabla(grupoAtributos);

		btnNuevoAtributo.addSelectionListener(this.tblAtributos.nuevo);
		btnEliminarAtributo.addSelectionListener(this.tblAtributos.eliminar);

		return dialogArea;
	}

	/**
	 * Carga la lista de atributos disponibles para establecer como origen de un
	 * <code>DERIVADO_COPIA</code>.
	 * 
	 * @return
	 */
	private String[] getAtributosDisponibles() {
		this.atributosOptions = new ArrayList<>(this.principal.getProyecto().getAtributosDiagrama());

		Collections.sort(this.atributosOptions);

		atributosOptions.remove(this.componente);

		List<String> atributosStr = new ArrayList<>();
		for (Atributo atributo : atributosOptions)
			atributosStr.add(atributo.getNombre());

		return atributosStr.toArray(new String[atributosStr.size()]);
	}

	@Override
	protected void cargarDatos() {
		this.txtNombre.setText(this.componente.getNombre());
		this.cboTipo.setText(this.componente.getTipo().name());
		this.txtCardinalidadMin.setText(this.componente.getCardinalidadMinima());
		this.txtCardinalidadMax.setText(this.componente.getCardinalidadMaxima());

		if (this.componente.getOriginal() != null)
			this.cboOriginal.setText(this.componente.getOriginal().getNombre());

		this.tblAtributos.setElementos(this.componente.getAtributos());

		this.habilitarControles();
	}

	@Override
	protected void aplicarCambios() {
		this.componente.setNombre(txtNombre.getText());
		this.componente.setTipo(TipoAtributo.valueOf(this.cboTipo.getText()));
		this.componente.setCardinalidadMinima(this.txtCardinalidadMin.getText());
		this.componente.setCardinalidadMaxima(this.txtCardinalidadMax.getText());

		if (this.cboOriginal.getSelectionIndex() >= 0)
			this.componente.setOriginal(this.atributosOptions.get(this.cboOriginal
					.getSelectionIndex()));

		for (Atributo atributo : this.tblAtributos.getElementos())
			this.componente.addAtributo(atributo);

		for (Atributo atributo : this.tblAtributos.getElementosEliminados())
			this.componente.removeAtributo(atributo);
	}

	@Override
	protected boolean validar(List<String> errors) {
		if (txtNombre.getText().trim().isEmpty())
			errors.add("Debe completar el nombre.");

		return errors.isEmpty();
	}

	private void habilitarControles() {
		TipoAtributo value = TipoAtributo.valueOf(cboTipo.getText());

		txtFormula.setEnabled(value == TipoAtributo.DERIVADO_CALCULO);
		cboOriginal.setEnabled(value == TipoAtributo.DERIVADO_COPIA);
	}

	private SelectionListener selectedTipo = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			habilitarControles();
		}
	};
}
