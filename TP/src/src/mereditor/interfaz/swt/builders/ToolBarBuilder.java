package mereditor.interfaz.swt.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mereditor.interfaz.swt.Principal;
import mereditor.interfaz.swt.figuras.DiagramaFigura;
import mereditor.interfaz.swt.listeners.AccionesProvider;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ToolBarBuilder implements Observer {
	private Principal principal;
	private ToolBar toolBar;
	private List<ToolItem> proyectoItems = new ArrayList<>();

	public static ToolBar build(Principal principal) {
		return new ToolBarBuilder(principal).toolBar;
	}

	private ToolBarBuilder(Principal principal) {
		this.toolBar = new ToolBar(principal.getShell(), SWT.HORIZONTAL
				| SWT.FLAT);
		this.principal = principal;
		this.principal.addObserver(this);
		this.init();
	}

	private void init() {
		ToolItem item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Nuevo Proyecto");
		item.setImage(Principal.getImagen("nuevo.png"));
		item.addSelectionListener(AccionesProvider.nuevo);

		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Abrir Proyecto");
		item.setImage(Principal.getImagen("abrir.png"));
		item.addSelectionListener(AccionesProvider.abrir);

		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Guardar Proyecto");
		item.setImage(Principal.getImagen("guardar.png"));
		item.addSelectionListener(AccionesProvider.guardar);
		proyectoItems.add(item);

		item = new ToolItem(this.toolBar, SWT.SEPARATOR);

		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Imprimir");
		item.setImage(Principal.getImagen("imprimir.png"));
		item.addSelectionListener(AccionesProvider.imprimir);
		proyectoItems.add(item);

		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Exportar");
		item.setImage(Principal.getImagen("exportar.png"));
		item.addSelectionListener(AccionesProvider.exportar);
		proyectoItems.add(item);

		item = new ToolItem(this.toolBar, SWT.SEPARATOR);

		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Nuevo Diagrama");
		item.setImage(Principal.getImagen("diagrama.png"));
		item.addSelectionListener(AccionesProvider.nuevoDiagrama);
		proyectoItems.add(item);

		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Agregar Entidad");
		item.setImage(Principal.getImagen("entidad.png"));
		item.addSelectionListener(AccionesProvider.agregarEntidad);
		proyectoItems.add(item);

		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Agregar Relacion");
		item.setImage(Principal.getImagen("relacion.png"));
		item.addSelectionListener(AccionesProvider.agregarRelacion);
		proyectoItems.add(item);

		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Agregar Jerarquia");
		item.setImage(Principal.getImagen("jerarquia.png"));
		item.addSelectionListener(AccionesProvider.agregarJerarquia);
		proyectoItems.add(item);
		
		item = new ToolItem(this.toolBar, SWT.SEPARATOR);

		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Validar Diagrama");
		item.setImage(Principal.getImagen("validar-diagrama.png"));
		item.addSelectionListener(AccionesProvider.validar);
		proyectoItems.add(item);
		
		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Validar Proyecto");
		item.setImage(Principal.getImagen("validar.png"));
		item.addSelectionListener(AccionesProvider.validarProyecto);
		proyectoItems.add(item);

		item = new ToolItem(this.toolBar, SWT.SEPARATOR);

		ToolItem zoomOutItem = new ToolItem(this.toolBar, SWT.PUSH);
		zoomOutItem.setToolTipText("Zoom -");
		zoomOutItem.setImage(Principal.getImagen("zoom-out.png"));
		zoomOutItem.addSelectionListener(AccionesProvider.zoomOut);
		proyectoItems.add(zoomOutItem);

		item = new ToolItem(this.toolBar, SWT.SEPARATOR);

		Combo cboZoom = new Combo(this.toolBar, SWT.READ_ONLY);
		cboZoom.setItems(DiagramaFigura.zoomOptions.keySet().toArray(
				new String[DiagramaFigura.zoomOptions.size()]));
		cboZoom.pack();
		cboZoom.setEnabled(false);
		cboZoom.setText(DiagramaFigura.zoom100);
		cboZoom.addSelectionListener(AccionesProvider.zoom);

		item.setWidth(cboZoom.getSize().x);
		item.setControl(cboZoom);
		proyectoItems.add(item);

		ToolItem zoomInItem = new ToolItem(this.toolBar, SWT.PUSH);
		zoomInItem.setToolTipText("Zoom +");
		zoomInItem.setImage(Principal.getImagen("zoom-in.png"));
		zoomInItem.addSelectionListener(AccionesProvider.zoomIn);
		proyectoItems.add(zoomInItem);
		
		/*
		 * Agregar el combo como la data de estos items para poder
		 * actualizar el valor del combo al presionarlos.
		 */
		zoomOutItem.setData(cboZoom);
		zoomInItem.setData(cboZoom);

		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Explorador del Proyecto");
		item.setImage(Principal.getImagen("tree_mode.png"));
		item.addSelectionListener(AccionesProvider.mostrarArbol);
		proyectoItems.add(item);

		item = new ToolItem(this.toolBar, SWT.SEPARATOR);

		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Convertir A Logico");
		item.setImage(Principal.getImagen("convertir.png"));
		item.addSelectionListener(AccionesProvider.convertir);
		proyectoItems.add(item);
		
		item = new ToolItem(this.toolBar, SWT.SEPARATOR);
		
		item = new ToolItem(this.toolBar, SWT.PUSH);
		item.setToolTipText("Salir");
		item.setImage(Principal.getImagen("salir.png"));
		item.addSelectionListener(AccionesProvider.salir);

		this.habilitarItems(false);
	}

	private void habilitarItems(boolean habilitar) {
		for (ToolItem item : this.proyectoItems) {
			item.setEnabled(habilitar);

			if (item.getControl() != null)
				item.getControl().setEnabled(habilitar);
		}
	}

	@Override
	public void update(Observable principal, Object arg) {
		this.habilitarItems(this.principal.getProyecto() != null);
	}
}