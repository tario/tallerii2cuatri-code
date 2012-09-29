package mereditor.interfaz.swt.editores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mereditor.control.JerarquiaControl;
import mereditor.modelo.Entidad;
import mereditor.modelo.Jerarquia;
import mereditor.modelo.Jerarquia.TipoJerarquia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

public class JerarquiaEditor extends Editor<Jerarquia> {
	public static final String GENERICA = "Generica";

	protected Combo cboTipo;
	protected Combo cboGenerica;
	protected EntidadTabla tblEntidadesDerivadas;

	protected List<String> entidadesNombres = new ArrayList<>();
	protected List<Entidad> entidades = new ArrayList<>();

	public JerarquiaEditor() {
		this(new JerarquiaControl());
	}

	public JerarquiaEditor(Jerarquia jerarquia) {
		super(jerarquia);
		this.titulo = "Editor - Jerarquia";
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

		this.cboTipo = createLabelCombo(header, TIPO);
		this.cboTipo.setItems(Editor.TiposJerarquias);

		this.cboGenerica = createLabelCombo(header, GENERICA);
		this.loadGenerica(this.cboGenerica);

		/**
		 * Entidades
		 */
		Group grupoDerivadas = new Group(dialogArea, SWT.NONE);
		grupoDerivadas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		grupoDerivadas.setText("Entidades Derivadas");
		grupoDerivadas.setLayout(new GridLayout(1, true));

		Composite botonesEntidades = new Composite(grupoDerivadas, SWT.NONE);
		botonesEntidades.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false,
				false, 1, 1));
		botonesEntidades.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button btnNuevaEntidad = new Button(botonesEntidades, SWT.PUSH);
		btnNuevaEntidad.setText(Editor.AGREGAR);

		Button btnEliminarEntidad = new Button(botonesEntidades,
				SWT.PUSH);
		btnEliminarEntidad.setText(Editor.ELIMINAR);

		// TableViewer
		this.tblEntidadesDerivadas = new EntidadTabla(grupoDerivadas);
		
		btnNuevaEntidad.addSelectionListener(this.tblEntidadesDerivadas.agregar);
		btnEliminarEntidad.addSelectionListener(this.tblEntidadesDerivadas.eliminar);

		return dialogArea;
	}

	/**
	 * Carga las entidades que pertenecen al diagrama actual y a sus padres en
	 * el combo.
	 */
	private void loadGenerica(Combo combo) {
		// Obtener las entidades de este diagrama
		this.entidades = new ArrayList<>(this.principal.getProyecto()
				.getEntidadesDiagrama());
		Collections.sort(this.entidades);

		for (Entidad entidad : this.entidades) {
			combo.add(entidad.getNombre());
			this.entidadesNombres.add(entidad.getNombre());
		}
	}

	@Override
	protected void cargarDatos() {
		this.cboTipo.setText(this.componente.getTipo().name());
		if (this.componente.getGenerica() != null)
			this.cboGenerica.setText(this.componente.getGenerica().getNombre());

		this.tblEntidadesDerivadas.setElementos(this.componente.getDerivadas());
	}

	@Override
	protected void aplicarCambios() {
		this.componente.setTipo(TipoJerarquia.valueOf(this.cboTipo.getText()));
		this.componente.setGenerica(this.entidades.get(this.cboGenerica.getSelectionIndex()));
		
		for(Entidad entidad : this.tblEntidadesDerivadas.getElementos())
			this.componente.addDerivada(entidad);
		
		for(Entidad entidad : this.tblEntidadesDerivadas.getElementosEliminados())
			this.componente.removeDerivada(entidad);
	}

	@Override
	protected boolean validar(List<String> errors) {
		if (this.cboGenerica.getSelectionIndex() < 0)
			errors.add("Debe seleccionar una entidad genérica.");

		if (this.tblEntidadesDerivadas.getElementos().isEmpty())
			errors.add("Debe seleccionar al menos una entidad derivada.");

		return errors.isEmpty();
	}
}