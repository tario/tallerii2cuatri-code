package mereditor.interfaz.swt.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mereditor.interfaz.swt.Principal;
import mereditor.interfaz.swt.listeners.AccionesProvider;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MenuBuilder implements Observer {
	private Principal principal;
	private Menu menuBar;
	private List<MenuItem> proyectoItems = new ArrayList<>();

	public static Menu build(Principal principal) {
		return new MenuBuilder(principal).menuBar;
	}

	private MenuBuilder(Principal principal) {
		this.menuBar = new Menu(principal.getShell(), SWT.BAR);
		this.principal = principal;
		this.principal.addObserver(this);
		this.init();
	}

	private void init() {
		/*
		 * Archivo
		 */
		MenuItem menuItem = new MenuItem(this.menuBar, SWT.CASCADE);
		menuItem.setText("&Archivo");

		Menu menu = new Menu(principal.getShell(), SWT.DROP_DOWN);
		menuItem.setMenu(menu);
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("&Nuevo");
		menuItem.addSelectionListener(AccionesProvider.nuevo);

		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("&Abrir");
		menuItem.addSelectionListener(AccionesProvider.abrir);

		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("&Guardar");
		menuItem.addSelectionListener(AccionesProvider.guardar);
		proyectoItems.add(menuItem);
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Guardar &como...");
		menuItem.addSelectionListener(AccionesProvider.guardarComo);
		proyectoItems.add(menuItem);
		
		menuItem = new MenuItem(menu, SWT.SEPARATOR);
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("&Exportar");
		menuItem.addSelectionListener(AccionesProvider.exportar);
		proyectoItems.add(menuItem);
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("&Imprimir");
		menuItem.addSelectionListener(AccionesProvider.imprimir);
		proyectoItems.add(menuItem);
		
		menuItem = new MenuItem(menu, SWT.SEPARATOR);

		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("&Salir");
		menuItem.addSelectionListener(AccionesProvider.salir);
		
		/*
		 * Ver
		 */
		
		menuItem = new MenuItem(this.menuBar, SWT.CASCADE);
		menuItem.setText("&Ver");
		
		menu = new Menu(principal.getShell(), SWT.DROP_DOWN);
		menuItem.setMenu(menu);
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Zoom ( &- )");
		menuItem.addSelectionListener(AccionesProvider.zoomOut);
		proyectoItems.add(menuItem);
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Zoom ( &+ )");
		menuItem.addSelectionListener(AccionesProvider.zoomIn);
		proyectoItems.add(menuItem);
		
		menuItem = new MenuItem(menu, SWT.SEPARATOR);
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("&Explorador del Proyecto");
		menuItem.addSelectionListener(AccionesProvider.mostrarArbol);
		proyectoItems.add(menuItem);
		
		/*
		 * Proyecto
		 */
		menuItem = new MenuItem(this.menuBar, SWT.CASCADE);
		menuItem.setText("&Proyecto");
		
		menu = new Menu(principal.getShell(), SWT.DROP_DOWN);
		menuItem.setMenu(menu);

		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("&Nuevo Diagrama");
		menuItem.addSelectionListener(AccionesProvider.nuevoDiagrama);
		proyectoItems.add(menuItem);
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Agregar &Entidad");
		menuItem.addSelectionListener(AccionesProvider.agregarEntidad);
		proyectoItems.add(menuItem);
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Agregar &Relacion");
		menuItem.addSelectionListener(AccionesProvider.agregarRelacion);
		proyectoItems.add(menuItem);

		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Agregar &Jerarquia");
		menuItem.addSelectionListener(AccionesProvider.agregarJerarquia);
		proyectoItems.add(menuItem);
		
		menuItem = new MenuItem(menu, SWT.SEPARATOR);
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("&Validar Diagrama");
		menuItem.addSelectionListener(AccionesProvider.validar);
		proyectoItems.add(menuItem);
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Validar &Proyecto");
		menuItem.addSelectionListener(AccionesProvider.validarProyecto);
		proyectoItems.add(menuItem);

		/*
		 * Ayuda
		 */
		menuItem = new MenuItem(menuBar, SWT.CASCADE);
		menuItem.setText("Ay&uda");

		menu = new Menu(principal.getShell(), SWT.DROP_DOWN);
		menuItem.setMenu(menu);

		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("&Sobre " + Principal.APP_NOMBRE + "...");

		principal.getShell().setMenuBar(menuBar);
		
		this.habilitarItems(false);
	}
	
	private void habilitarItems(boolean habilitar) {
		for (MenuItem item : this.proyectoItems)
			item.setEnabled(habilitar);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.habilitarItems(this.principal.getProyecto() != null);		
	}
}
