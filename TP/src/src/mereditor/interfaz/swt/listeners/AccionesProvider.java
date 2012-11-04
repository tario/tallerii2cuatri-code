package mereditor.interfaz.swt.listeners;

import mereditor.interfaz.swt.Principal;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.ToolItem;

public class AccionesProvider {

	private static Principal principal() {
		return Principal.getInstance();
	}

	/**
	 * Nuevo proyecto.
	 */
	public static final SelectionListener nuevo = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			principal().nuevoProyecto();
		}
	};

	/**
	 * Abrir Proyecto.
	 */
	public static final SelectionListener abrir = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			principal().abrirProyecto();
		}
	};

	/**
	 * Guardar proyecto.
	 */
	public static final SelectionListener guardar = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			principal().guardarProyecto();
		}
	};

	public static final SelectionListener guardarComo = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			principal().guardarProyecto(true);
		}
	};

	public static final SelectionListener imprimir = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			principal().imprimir();
		};
	};

	public static final SelectionListener exportar = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			principal().exportar();
		};
	};

	public static final SelectionListener zoomIn = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			ToolItem item = (ToolItem) e.getSource();
			principal().zoomIn((Combo) item.getData());
		};
	};

	public static final SelectionListener zoomOut = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			ToolItem item = (ToolItem) e.getSource();
			principal().zoomOut((Combo) item.getData());
		};
	};

	public static SelectionListener zoom = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Combo combo = (Combo) e.getSource();
			principal().zoom(combo.getText());
		};
	};

	/**
	 * Agregar una entidad al diagrama actual.
	 */
	public static final SelectionListener agregarEntidad = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			principal().agregarEntidad();
		}
	};

	/**
	 * Agregar una Relacion al diagrama actual.
	 */
	public static final SelectionListener agregarRelacion = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			principal().agregarRelacion();
		};
	};

	/**
	 * Agregar una Jerarquia al diagrama actual.
	 */
	public static final SelectionListener agregarJerarquia = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			principal().agregarJerarquia();
		};
	};

	/**
	 * Validar el diagrama actual.
	 */
	public static final SelectionListener validar = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			principal().validar();
		};
	};

	/**
	 * Validar el proyecto actual.
	 */
	public static final SelectionListener validarProyecto = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			principal().validarProyecto();
		};
	};

	/**
	 * Mostrar arbol.
	 */
	public static final SelectionListener mostrarArbol = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Principal.getInstance().mostrarArbol(true);
		}
	};

	/**
	 * Agregar un nuevo diagrama al actual.
	 */
	public static final SelectionListener nuevoDiagrama = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			principal().agregarDiagrama();
		}
	};

	/**
	 * Salir de la aplicaci�ｽn.
	 */
	public static final SelectionListener salir = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			principal().salir();
		}
	};
	
	/**
	 * Convertir DER a Logico
	 */
	public static final SelectionListener convertir = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			principal().convertir();
		}
	};

}
