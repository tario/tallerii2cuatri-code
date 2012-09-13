package mereditor.interfaz.swt.editores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public abstract class Tabla<T> extends TableViewer {
	/**
	 * Indica si se pueden editar los valores directamente en la tabla.
	 */
	protected boolean readOnly = false;
	protected List<T> elementos = new ArrayList<>();
	protected List<T> elementosEliminados = new ArrayList<>();

	protected List<String> columnas = new ArrayList<>();
	protected List<CellEditor> editoresCeldas = new ArrayList<>();

	/**
	 * Contiene la seleccion actual.
	 */
	protected IStructuredSelection selection = null;

	public Tabla(Composite parent) {
		super(parent, SWT.FULL_SELECTION);
		this.init();
	}

	private void init() {
		this.initColumnas();

		this.setContentProvider(this.contentProvider);
		this.setLabelProvider(this.labelProvider);
		this.setInput(this.elementos);

		// Configurar tabla
		Table table = this.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		for (String property : this.columnas)
			new TableColumn(table, SWT.CENTER).setText(property);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Crear editores de celda
		this.initEditorsCeldas(table);

		// Establecer el titulo de las columnas, los modificadores y los
		// editores.
		this.setColumnProperties((String[]) columnas
				.toArray(new String[columnas.size()]));
		this.setCellModifier(this.cellModifier);
		this.setCellEditors((CellEditor[]) editoresCeldas
				.toArray(new CellEditor[editoresCeldas.size()]));

		this.addSelectionChangedListener(this.seleccion);
		this.addDoubleClickListener(this.dobleClick);
	}

	/**
	 * Agrega los nombres de las columnas que se mostraran en la tabla.
	 */
	protected abstract void initColumnas();

	protected abstract void initEditorsCeldas(Table table);

	protected abstract String getTextoColumna(T element, int columnIndex);

	protected abstract Object getValorCelda(T element, String property);

	protected abstract void setValorCelda(T element, String property,
			Object value);

	/**
	 * Debe ser implementada para agregar nuevos elementos.
	 * 
	 * @return
	 */
	protected abstract T nuevoElemento();

	/**
	 * Debe ser implementada para agregar elementos existentes.
	 * 
	 * @return
	 */
	protected abstract T agregarElemento();

	/**
	 * Debe ser implementada para abrir el editor del tipo de elemento.
	 * 
	 * @param elemento
	 */
	protected abstract void abrirEditor(T elemento);

	public void setElementos(Collection<T> elementos) {
		this.elementos.addAll(elementos);
		this.refresh();

		for (TableColumn column : this.getTable().getColumns())
			column.pack();
	}

	public List<T> getElementos() {
		return this.elementos;
	}

	public List<T> getElementosEliminados() {
		return this.elementosEliminados;
	}

	/**
	 * Indica si se pueden editar los valores directamente en la tabla.
	 * 
	 * @return <code>true</code> si la tabla es de sólo lectura.
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	private final ISelectionChangedListener seleccion = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent event) {
			selection = (IStructuredSelection) getSelection();
		}
	};

	/**
	 * Agregar elemento existente.
	 */
	public final SelectionListener agregar = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			T elemento = agregarElemento();
			if (elemento != null) {
				elementos.add(elemento);
				refresh();
			}
		}
	};

	/**
	 * Agregar un nuevo elemento.
	 */
	public final SelectionListener nuevo = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			T elemento = nuevoElemento();
			if (elemento != null) {
				elementos.add(elemento);
				refresh();
			}
		}
	};

	/**
	 * Eliminar elemento
	 */
	public final SelectionListener eliminar = new SelectionAdapter() {
		@SuppressWarnings("unchecked")
		public void widgetSelected(SelectionEvent event) {
			if (selection != null) {
				T elemento = (T) selection.getFirstElement();

				elementos.remove(elemento);
				elementosEliminados.add(elemento);

				refresh();
			}
		}
	};

	private final IContentProvider contentProvider = new IStructuredContentProvider() {
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return ((List<?>) inputElement).toArray();
		}
	};

	private final IBaseLabelProvider labelProvider = new ITableLabelProvider() {

		@Override
		public void removeListener(ILabelProviderListener listener) {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		@SuppressWarnings("unchecked")
		@Override
		public String getColumnText(Object element, int columnIndex) {
			return getTextoColumna((T) element, columnIndex);
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	};

	private ICellModifier cellModifier = new ICellModifier() {
		public boolean canModify(Object element, String property) {
			return !readOnly;
		}

		@SuppressWarnings("unchecked")
		public Object getValue(Object element, String property) {
			return getValorCelda((T) element, property);
		}

		@SuppressWarnings("unchecked")
		public void modify(Object element, String property, Object value) {
			if (element instanceof Item)
				element = ((Item) element).getData();
			setValorCelda((T) element, property, value);

			refresh();
		}
	};

	private IDoubleClickListener dobleClick = new IDoubleClickListener() {
		@SuppressWarnings("unchecked")
		@Override
		public void doubleClick(DoubleClickEvent event) {
			if (!readOnly && selection != null) {
				T elemento = (T) selection.getFirstElement();
				abrirEditor(elemento);

				refresh();
			}
		}
	};
}
