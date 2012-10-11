package mereditor.interfaz.swt.editores;

import java.util.List;

import mereditor.control.EntidadControl;
import mereditor.modelo.Atributo;
import mereditor.modelo.Entidad;
import mereditor.modelo.Entidad.Identificador;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

public class IdentificadorEditor extends Editor<Identificador> {
	protected AtributosTabla tblAtributos;
	protected EntidadTabla tblEntidades;
	
	/**
	 * Constructor para el editor visual
	 * 
	 * @wbp.parser.constructor
	 */
	protected IdentificadorEditor(Shell shell) {
		super(shell);
		this.componente = new EntidadControl().new Identificador(new EntidadControl());
	}

	public IdentificadorEditor(Identificador componente) {
		super(componente);
		this.titulo = "Editor - Identificador de "
				+ componente.getEntidad().getNombre();
	}

	public IdentificadorEditor(Entidad entidad) {
		this(entidad.new Identificador(entidad));
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);

		/**
		 * Atributos.
		 */
		Group grupoAtributos = new Group(dialogArea, SWT.NONE);
		grupoAtributos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		grupoAtributos.setText("Atributos");
		grupoAtributos.setLayout(new GridLayout(1, true));
		
		Composite botonesAtributos = new Composite(grupoAtributos, SWT.NONE);
		botonesAtributos.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		botonesAtributos.setLayout(new RowLayout(SWT.HORIZONTAL));		

		Button btnNuevoAtributo = new Button(botonesAtributos, SWT.PUSH);
		btnNuevoAtributo.setText(Editor.AGREGAR);

		Button btnEliminarAtributo = new Button(botonesAtributos, SWT.PUSH);
		btnEliminarAtributo.setText(Editor.ELIMINAR);

		// TableViewer
		this.tblAtributos = new AtributosTabla(grupoAtributos, this.componente.getEntidad());
		this.tblAtributos.setReadOnly(true);
		
		btnNuevoAtributo.addSelectionListener(this.tblAtributos.agregar);
		btnEliminarAtributo.addSelectionListener(this.tblAtributos.eliminar);

		/**
		 * Entidades
		 */
		Group grupoEntidades = new Group(dialogArea, SWT.NONE);
		grupoEntidades.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		grupoEntidades.setText("Entidades");
		grupoEntidades.setLayout(new GridLayout(1, true));
		
		Composite botonesEntidades = new Composite(grupoEntidades, SWT.NONE);
		botonesEntidades.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false, 1, 1));
		botonesEntidades.setLayout(new RowLayout(SWT.HORIZONTAL));		

		Button btnNuevoEntidad = new Button(botonesEntidades, SWT.PUSH);
		btnNuevoEntidad.setText(Editor.AGREGAR);

		Button btnEliminarEntidad = new Button(botonesEntidades, SWT.PUSH);
		btnEliminarEntidad.setText(Editor.ELIMINAR);
		
		this.tblEntidades = new EntidadTabla(grupoEntidades);
		
		btnNuevoEntidad.addSelectionListener(this.tblEntidades.agregar);
		btnEliminarEntidad.addSelectionListener(this.tblEntidades.eliminar);

		return dialogArea;
	}

	@Override
	protected void cargarDatos() {
		this.tblAtributos.setElementos(this.componente.getAtributos());
		this.tblEntidades.setElementos(this.componente.getEntidades());
	}

	@Override
	protected void aplicarCambios() {
		for (Atributo atributo : this.tblAtributos.getElementos())
			componente.addAtributo(atributo);

		for (Atributo atributo : this.tblAtributos.getElementosEliminados())
			componente.removeAtributo(atributo);
		
		for (Entidad entidad : this.tblEntidades.getElementos())
			componente.addEntidad(entidad);

		for (Entidad entidad : this.tblEntidades.getElementosEliminados())
			componente.removeEntidad(entidad);
	}

	@Override
	protected boolean validar(List<String> errors) {
		if (this.tblAtributos.getElementos().isEmpty()
				&& this.tblEntidades.getElementos().isEmpty())
			errors.add("El identificador debe tener por lo menos un atributo o una entidad.");
		
		if(this.tblEntidades.getElementos().contains(this.componente.getEntidad()))
			errors.add("La misma entidad no puede ser su propio identificador.");

		return errors.isEmpty();
	}
}
