package mereditor.interfaz.swt.builders;

import mereditor.control.Control;
import mereditor.control.TablaControl;
import mereditor.interfaz.swt.Principal;
import mereditor.modelo.Diagrama;
import mereditor.modelo.Proyecto;
import mereditor.modelo.base.Componente;
import mreleditor.modelo.DiagramaLogico;
import mreleditor.modelo.Tabla;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeManager {
	private static Tree tree;
	private static CTabItem tab;
	private static CTabFolder folder;
	private static TreeItem diagramaActivo;
	private static TreeItem diagramaLogicoActivo;

	public static Tree build(Composite composite) {
		new TreeManager(composite);
		return TreeManager.tree;
	}

	private TreeManager(Composite composite) {
		folder = new CTabFolder(composite, SWT.CENTER);
		tab = new CTabItem(folder, SWT.BOTTOM);
		tree = new Tree(folder, SWT.NO_SCROLL);
		new MenuArbolBuilder(tree);

		this.init();
	}
	
	private void init() {
		tree.setBackground(new Color(null, 240, 240, 240));

		folder.setSimple(false);
		folder.setSelection(tab);
		folder.setMinimizeVisible(true);
		folder.addCTabFolder2Listener(this.minimizar);

		tab.setControl(tree);
		tab.setShowClose(false);
	}

	/**
	 * Agregado del diagrama principal y sus hijos.
	 * 
	 * @param diagrama
	 * @param tree
	 */
	
	private static void agregar(Diagrama diagrama, Tree tree){
		 agregar( diagrama,  tree,null);
	}
	private static void agregar(Diagrama diagrama, Tree tree, Proyecto proyecto) {
		tree.removeAll();
		tree.setData(diagrama);
		// Crear el item raiz.
		TreeItem item = new TreeItem(tree, SWT.NULL);
		item.setText(diagrama.getNombre());
		item.setData(diagrama);
		// Cargar el icono del item.
		String nombreIcono = ((Control<?>) diagrama).getNombreIcono();
		item.setImage(Principal.getIcono(nombreIcono));

		diagramaActivo = item;

		for (Diagrama diagramaHijo : diagrama.getDiagramas())
			agregar(diagramaHijo, item,proyecto);
		

		for (Componente componente : diagrama.getEntidades(false))
			agregar(componente, item);

		for (Componente componente : diagrama.getRelaciones(false))
			agregar(componente, item);

		for (Componente componente : diagrama.getJerarquias(false))
			agregar(componente, item);
		
		for(Componente componente:proyecto.getComponentes())
			if(DiagramaLogico.class.isInstance(componente)){
				DiagramaLogico logico=(DiagramaLogico)componente;
				if(diagrama.getId().equals(logico.getDer().getId()))
					agregarLogico(logico, item);
			}
						
			

		item.setExpanded(true);
		
	}
	private final static String DIAGRAMA_LOGICO_TEXT="Diagrama Logico";
	private static void agregarLogico(DiagramaLogico diagrama, TreeItem tree) {
		// Crear el item raiz.
		TreeItem item = new TreeItem(tree, SWT.NULL);
		item.setText(DIAGRAMA_LOGICO_TEXT);
		item.setData(diagrama);
		// Cargar el icono del item.
		String nombreIcono = ((Control<?>) diagrama).getNombreIcono();
		item.setImage(Principal.getIcono(nombreIcono));

		diagramaLogicoActivo = item;


		for (Componente componente : diagrama.getTablas()){
			TablaControl tabControl = new TablaControl((Tabla)componente);
			agregar(tabControl, item);
		}
		
		item.setExpanded(true);
	}
	
	/**
	 * Agregado de diagrama no principal y sus hijos.
	 * 
	 * @param diagrama
	 * @param padre
	 */
	private static void agregar(Diagrama diagrama, TreeItem padre, Proyecto proyecto) {
		TreeItem item = new TreeItem(padre, SWT.NULL);
		item.setText(diagrama.getNombre());
		item.setData(diagrama);
		String nombreIcono = ((Control<?>) diagrama).getNombreIcono();
		item.setImage(Principal.getIcono(nombreIcono));

		for (Diagrama diagramaHijo : diagrama.getDiagramas())
			agregar(diagramaHijo, item);

		for (Componente componente : diagrama.getEntidades(false))
			agregar(componente, item);

		for (Componente componente : diagrama.getRelaciones(false))
			agregar(componente, item);

		for (Componente componente : diagrama.getJerarquias(false))
			agregar(componente, item);
		
		for(Componente componente:proyecto.getComponentes())
			if(DiagramaLogico.class.isInstance(componente)){
				DiagramaLogico logico=(DiagramaLogico)componente;
				if(diagrama.getId().equals(logico.getDer().getId()))
					agregarLogico(logico, item);
			}
		
		
	}

	/**
	 * Agregado de componente.
	 * 
	 * @param diagrama
	 * @param item
	 */
	private static void agregar(Componente componente, TreeItem padre) {
		TreeItem item = new TreeItem(padre, SWT.NULL);
		item.setText(componente.toString());
		item.setData(componente);

		String icono = ((Control<?>) componente).getNombreIcono();
		item.setImage(Principal.getIcono(icono));
	}

	/**
	 * Carga todo el arbol de componentes entero.
	 * @param proyecto
	 */
	public static void cargar(Proyecto proyecto) {
		TreeManager.agregar(proyecto.getDiagramaRaiz(), TreeManager.tree, proyecto);
		tab.setText(proyecto.getNombre());
		folder.setEnabled(true);
	}

	public static void agregarADiagramaActual(Diagrama nuevoDiagrama) {
		agregar(nuevoDiagrama, diagramaActivo);
	}
	
	public static void agregarADiagramaActual(DiagramaLogico diagrama) {
		agregarLogico(diagrama, diagramaActivo);
	}

	public static void agregarADiagramaActual(Componente nuevoComponente) {
		agregar(nuevoComponente, diagramaActivo);
	}
	
	public static Diagrama getDiagramaActual() {
		return (Diagrama) diagramaActivo.getData();
	}

	public static void setDiagramaActivo(TreeItem diagramaActivo) {
		TreeManager.diagramaActivo = diagramaActivo;
		TreeItem[] hijos= diagramaActivo.getItems();
		diagramaLogicoActivo=null;
		for(int i=0; i<hijos.length;i++){
			if(hijos[i].getText().equals(DIAGRAMA_LOGICO_TEXT))
				TreeManager.diagramaLogicoActivo = hijos[i];
		}
	}
	public static void borrarLogicoActivo(){
		if( diagramaLogicoActivo != null)
		diagramaLogicoActivo.dispose();
	}
	
	public static void setDiagramaLogicoActivo(TreeItem diagramaActivo) {
		TreeManager.diagramaLogicoActivo = diagramaActivo;
	}
	
	private CTabFolder2Listener minimizar = new CTabFolder2Adapter() {
		public void minimize(CTabFolderEvent event) {
			Principal.getInstance().mostrarArbol(false);
		};
	};
}
