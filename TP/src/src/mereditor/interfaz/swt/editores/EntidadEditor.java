package mereditor.interfaz.swt.editores;

import java.util.List;

import mereditor.control.EntidadControl;
import mereditor.modelo.Atributo;
import mereditor.modelo.Entidad;
import mereditor.modelo.Entidad.TipoEntidad;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
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

public class EntidadEditor extends Editor<Entidad> {
	protected Text txtNombre;
	protected Combo cboTipo;
	protected AtributosTabla tblAtributos;
	protected IdentificadorTabla tblIdentificadores;

	/**
	 * Constructor para el editor visual
	 * 
	 * @wbp.parser.constructor
	 */
	protected EntidadEditor(Shell shell) {
		super(shell);
		this.componente = new EntidadControl();
	}

	public EntidadEditor() {
		this(new EntidadControl());
	}

	public EntidadEditor(Entidad entidad) {
		super(entidad);
		this.titulo = "Editor - " + componente.getNombre();
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(400, 600);
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

		this.cboTipo = createLabelCombo(header, TIPO);
		this.cboTipo.setItems(Editor.TiposEntidades);

		/**
		 * Atributos.
		 */
		Group grupoAtributos = new Group(dialogArea, SWT.NONE);
		grupoAtributos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		grupoAtributos.setText(Editor.ATRIBUTOS);
		grupoAtributos.setLayout(new GridLayout(1, true));

		Composite botones = new Composite(grupoAtributos, SWT.NONE);
		botones.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		botones.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button btnNuevoAtributo = new Button(botones, SWT.PUSH);
		btnNuevoAtributo.setText(Editor.NUEVO);

		Button btnEliminarAtributo = new Button(botones, SWT.PUSH);
		btnEliminarAtributo.setText(Editor.ELIMINAR);

		/*
		 * ExpandBar expandBar = new ExpandBar(botones, SWT.NONE);
		 * expandBar.setLayoutData(new RowData(98, SWT.DEFAULT));
		 * expandBar.setSpacing(6);
		 */

		// TableViewer
		this.tblAtributos = new AtributosTabla(grupoAtributos);

		btnNuevoAtributo.addSelectionListener(this.tblAtributos.nuevo);
		btnEliminarAtributo.addSelectionListener(this.tblAtributos.eliminar);

		/**
		 * Identificadores.
		 */
		Group grupoIdentificadores = new Group(dialogArea, SWT.NONE);
		grupoIdentificadores.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		grupoIdentificadores.setText(Editor.IDENTIFICADORES);
		grupoIdentificadores.setLayout(new GridLayout(1, true));
		
		Composite botonesIdentificadores = new Composite(grupoIdentificadores, SWT.NONE);
		botonesIdentificadores.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		botonesIdentificadores.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button btnNuevoIdentificador = new Button(botonesIdentificadores, SWT.RIGHT);
		btnNuevoIdentificador.setText(Editor.NUEVO);
		
		Button btnEliminarIdentificador = new Button(botonesIdentificadores, SWT.RIGHT);
		btnEliminarIdentificador.setText(Editor.ELIMINAR);

		this.tblIdentificadores = new IdentificadorTabla(grupoIdentificadores, this.componente);

		btnNuevoIdentificador.addSelectionListener(this.tblIdentificadores.nuevo);
		btnEliminarIdentificador.addSelectionListener(this.tblIdentificadores.eliminar);

		return dialogArea;
	}

	@Override
	protected void cargarDatos() {
		this.txtNombre.setText(this.componente.getNombre());
		this.cboTipo.setText(this.componente.getTipo().name());

		this.tblAtributos.setElementos(this.componente.getAtributos());
		this.tblIdentificadores.setElementos(this.componente.getIdentificadores());
	}

	@Override
	protected void aplicarCambios() {
		componente.setNombre(txtNombre.getText());
		componente.setTipo(TipoEntidad.valueOf(this.cboTipo.getText()));

		for (Atributo atributo : this.tblAtributos.getElementos())
			componente.addAtributo(atributo);

		for (Atributo atributo : this.tblAtributos.getElementosEliminados())
			componente.removeAtributo(atributo);
		
		for (Entidad.Identificador identificador : this.tblIdentificadores.getElementos())
			componente.addIdentificador(identificador);

		for (Entidad.Identificador identificador : this.tblIdentificadores.getElementosEliminados())
			componente.removeIdentificador(identificador);
	}

	@Override
	protected boolean validar(List<String> errors) {
		if (this.txtNombre.getText().length() == 0)
			errors.add("Debe completar el nombre.");

		return errors.isEmpty();
	}
}
