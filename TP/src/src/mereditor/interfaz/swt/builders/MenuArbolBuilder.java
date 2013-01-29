package mereditor.interfaz.swt.builders;

import mereditor.interfaz.swt.Principal;
import mereditor.interfaz.swt.editores.EditorFactory;
import mereditor.modelo.Diagrama;
import mereditor.modelo.base.Componente;
import mreleditor.modelo.DiagramaLogico;
import mreleditor.modelo.Tabla;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class MenuArbolBuilder {
	private Tree arbol;
	private Menu menu;
	/**
	 * Opción Pegar del menú desplegable.
	 */
	private MenuItem menuItemPegar;
	/**
	 * Mantiene el valor del item copiado o cortado.
	 */
	private TreeItem papelera;

	public MenuArbolBuilder(Tree arbol) {
		this.arbol = arbol;
		this.menu = new Menu(this.arbol.getShell(), SWT.POP_UP);

		this.init();
	}

	private void init() {
		this.arbol.addListener(SWT.MouseDown, this.mostrar);
		this.arbol.addListener(SWT.MouseDoubleClick, this.abrir);

		MenuItem item = new MenuItem(menu, SWT.DROP_DOWN);
		item.setText("Editar");
		item.addListener(SWT.Selection, this.editar);

		item = new MenuItem(menu, SWT.DROP_DOWN);
		item.setText("Cortar");
		item.addListener(SWT.Selection, this.cortar);

		this.menuItemPegar = new MenuItem(menu, SWT.DROP_DOWN);
		this.menuItemPegar.setText("Pegar");
		this.menuItemPegar.setEnabled(false);
		this.menuItemPegar.addListener(SWT.Selection, this.pegar);

		item = new MenuItem(menu, SWT.DROP_DOWN);
		item.setText("Eliminar");
		item.addListener(SWT.Selection, this.eliminar);
	}

	/**
	 * Devuelve el item que se encuentra seleccionado.
	 * 
	 * @return
	 */
	private TreeItem getItem() {
		if (this.arbol.getSelectionCount() == 1)
			return this.arbol.getSelection()[0];

		return null;
	}

	/**
	 * Devuelve el componente asociado al item que se encuentra seleccionado.
	 * 
	 * @return
	 */
	private Componente getComponente() {
		TreeItem item = this.getItem();

		if (item != null)
			return (Componente) item.getData();

		return null;
	}

	/**
	 * Habilita las opciones del menú según las operaciones disponibles.
	 */
	private void habilitarOpciones() {
		menuItemPegar.setEnabled(this.papelera != null);
	}

	/**
	 * Muestra el menu contextual en la posición del item sobre el que se
	 * presionó el botón derecho.
	 */
	public final Listener mostrar = new Listener() {
		@Override
		public void handleEvent(Event event) {

			if (!(DiagramaLogico.class.isInstance(getComponente()))
					&& !(Tabla.class.isInstance(getComponente()))) {

				// Mostrar el menu si no es el boton derecho
				if (event.button > 1) {
					TreeItem treeItem = getItem();

					if (treeItem != null) {
						Rectangle area = treeItem.getBounds();
						Point posicion = new Point(area.x, area.y + area.height);
						posicion = arbol.toDisplay(posicion);
						menu.setLocation(posicion.x, posicion.y);
						menu.setVisible(true);
						habilitarOpciones();
					}
				}
			}
		}
	};

	/**
	 * Abre el editor del componente a menos que sea un diagrama.
	 */
	public final Listener abrir = new Listener() {
		@Override
		public void handleEvent(Event event) {
			Componente componente = getComponente();
			if (Diagrama.class.isInstance(componente)) {
				Principal.getInstance().abrirDiagrama(componente.getId());
				getItem().setExpanded(true);
				TreeManager.setDiagramaActivo(getItem());
			} else {
				if (DiagramaLogico.class.isInstance(componente)) {
					Principal.getInstance().abrirDiagramaLogico(
							componente.getId());
					getItem().setExpanded(true);
					TreeManager.setDiagramaLogicoActivo(getItem());
				} else if (!Tabla.class.isInstance(componente))
					EditorFactory.getEditor(componente).open();
			}

		}
	};

	/**
	 * Abre el editor del componente
	 */
	public final Listener editar = new Listener() {
		@Override
		public void handleEvent(Event event) {
			EditorFactory.getEditor(getComponente()).open();
		}
	};

	/**
	 * Cortar el item actual.
	 */
	public final Listener cortar = new Listener() {
		@Override
		public void handleEvent(Event event) {
			papelera = getItem();
		}
	};

	/**
	 * Eliminar componente e item seleccionado.
	 */
	public final Listener eliminar = new Listener() {
		@Override
		public void handleEvent(Event event) {
			Componente componente = getComponente();
			TreeItem current = getItem();
			TreeItem parent = current.getParentItem();
			if (parent != null) {
				Diagrama diagrama = (Diagrama) parent.getData();
				diagrama.eliminar(componente);

				Principal.getInstance().actualizarVista();
				current.dispose();
			}
		}
	};

	/**
	 * Pegar item en la papelera.
	 */
	public final Listener pegar = new Listener() {
		@Override
		public void handleEvent(Event event) {
			if (validarPegar()) {
				TreeItem nuevoItem = new TreeItem(getItem(), SWT.NULL);
				nuevoItem.setText(papelera.getText());
				nuevoItem.setData(papelera.getData());
				nuevoItem.setImage(papelera.getImage());

				pegarHijos(nuevoItem, papelera);

				papelera.dispose();
				papelera = null;
			}
		}
	};

	private boolean validarPegar() {
		TreeItem current = getItem();
		if (this.papelera == null && this.papelera == current
				&& this.papelera.isDisposed()) {
			return false;
		}

		boolean valido = true;
		while (valido && (this.arbol.indexOf(current) < 0)) {
			valido = current != this.papelera;
			current = current.getParentItem();
		}
		return valido;
	}

	private void pegarHijos(TreeItem itemDestino, TreeItem itemOrigen) {
		TreeItem[] hijos = itemOrigen.getItems();
		TreeItem destinoActual = null;
		for (int i = 0; i < hijos.length; i++) {
			destinoActual = new TreeItem(itemDestino, SWT.NONE);
			destinoActual.setText(hijos[i].getText());
			pegarHijos(destinoActual, hijos[i]);
		}
	}
}
