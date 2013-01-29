package mereditor.interfaz.swt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import mereditor.control.DiagramaControl;
import mereditor.control.DiagramaLogicoControl;
import mereditor.interfaz.swt.builders.DialogBuilder;
import mereditor.interfaz.swt.builders.DialogBuilder.PromptResult;
import mereditor.interfaz.swt.builders.DialogBuilder.Resultado;
import mereditor.interfaz.swt.builders.MenuBuilder;
import mereditor.interfaz.swt.builders.ToolBarBuilder;
import mereditor.interfaz.swt.builders.TreeManager;
import mereditor.interfaz.swt.dialogs.AgregarEntidadDialog;
import mereditor.interfaz.swt.dialogs.AgregarJerarquiaDialog;
import mereditor.interfaz.swt.dialogs.AgregarRelacionDialog;
import mereditor.interfaz.swt.figuras.DiagramaFigura;
import mereditor.interfaz.swt.figuras.DiagramaLogicoFigura;
import mereditor.modelo.Proyecto;
import mereditor.modelo.ProyectoProxy;
import mereditor.modelo.Validacion.EstadoValidacion;
import mereditor.modelo.validacion.Observacion;
import mereditor.xml.SaverLoaderXML;
import mreleditor.conversor.ConversorDERRepresentacion;
import mreleditor.conversor.ConversorDERaLogico;
import mreleditor.modelo.DiagramaLogico;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PrintFigureOperation;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.w3c.dom.Document;

/**
 * Formulario principal de la aplicacion.
 * 
 */
public class Principal extends Observable implements FigureListener {
	/**
	 * Color predeterminado del ç–µea principal del diagrama.
	 */
	public static final Color defaultBackgroundColor = new Color(null, 255, 255, 255);
	/**
	 * Tå’œulo a mostrar de la aplicaciî‰¢.
	 */
	public static final String APP_NOMBRE = "MER Editor";
	/**
	 * Tå’œulo del pop-up "Guardar cambios"
	 */
	private static final String TITULO_GUARDAR_DIAGRAMA_ACTUAL = "Informaciî‰¢";
	/**
	 * Mensaje del pop-up "Guardar cambios"
	 */
	private static final String MENSAJE_GUARDAR_DIAGRAMA_ACTUAL = "ï½¿Desea guardar los cambios hechos al diagrama actual?";
	/**
	 * Extensiî‰¢ de los archivos del proyecto.
	 */
	public static final String[] extensionProyecto = new String[] { "*.xml" };
	/**
	 * Extensiî‰¢ de los archivos del validaciî‰¢.
	 */
	public static final String[] extensionValidacion = new String[] { "*.txt" };
	/**
	 * Extensiî‰¢ de la imagen a exportar.
	 */
	public static final String[] extensionesImagen = new String[] { "*.jpg" };
	/**
	 * Ubicaciî‰¢ de los recursos de imç–Šenes.
	 */
	private static final String PATH_IMAGENES = "/recursos/imagenes/";
	/**
	 * Ubicaciî‰¢ de los recursos de iconos.
	 */
	private static final String PATH_ICONOS = "/recursos/iconos/";
	/**
	 * Formato de fecha.
	 */
	private static final Format dateFormat = new SimpleDateFormat("yyMMdd");

	/**
	 * Singleton de la clase Principal
	 */
	private static Principal instancia;

	/**
	 * Punto de entrada de la aplicaciî‰¢.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);

		Principal.instancia = new Principal(shell);

		while (!shell.isDisposed())
			while (!display.readAndDispatch())
				display.sleep();
	}

	/**
	 * Devuelve el singleton de la clase Principal.
	 * 
	 * @return
	 */
	public static Principal getInstance() {
		return Principal.instancia;
	}

	/**
	 * Devuelve un recurso de imagen.
	 * 
	 * @param nombre
	 *            Nombre del archivo de imagen.
	 * @return
	 */
	public static Image getImagen(String nombre) {
		return loadImagen(PATH_IMAGENES + nombre);
	}

	/**
	 * Devuelve un recurso de icono.
	 * 
	 * @param nombre
	 *            Nombre del archivo de icono.
	 * @return
	 */
	public static Image getIcono(String nombre) {
		return loadImagen(PATH_ICONOS + nombre);
	}

	private static Image loadImagen(String path) {
		Image img = new Image(Display.getDefault(), Principal.class.getResourceAsStream(path));
		return img;
	}

	/**
	 * Shell de SWT de la aplicaciî‰¢.
	 */
	private Shell shell;

	private SashForm sashForm;
	private ToolBar toolBar;
	private FigureCanvas figureCanvas;
	private Label lblStatus;

	/**
	 * Figura sobre la que se dibuja el diagrama.
	 */
	private DiagramaFigura panelDiagrama;
	private DiagramaLogicoFigura panelDiagramaLogico;
	/**
	 * Proyecto que se encuentra abierto.
	 */
	private Proyecto proyecto;

	/**
	 * Handler del evento cuando se cierra la aplicaciî‰¢. Si hay modificaciones
	 * pendientes de ser guardadas muestra el prompt.
	 */
	private Listener promptClose = new Listener() {
		@Override
		public void handleEvent(Event event) {
			int resultado = preguntarGuardar();
			event.doit = resultado != SWT.CANCEL;
		}
	};

	/**
	 * Constructor
	 * 
	 * @param shell
	 */
	private Principal(Shell shell) {
		this.shell = shell;
		this.shell.setMaximized(true);
		this.shell.setText(APP_NOMBRE);
		this.shell.setLayout(new FormLayout());
		this.shell.addListener(SWT.Close, this.promptClose);
		this.shell.setImage(getImagen("diagrama.png"));

		// Construir y agregar los controles.
		MenuBuilder.build(this);
		this.toolBar = ToolBarBuilder.build(this);
		this.sashForm = new SashForm(this.shell, SWT.HORIZONTAL);
		TreeManager.build(this.sashForm);
		this.lblStatus = new Label(shell, SWT.BORDER);

		this.initFigureCanvas();

		this.arregloLayout();

		this.shell.open();
	}

	/**
	 * Establece el layout de los diferentes widgets en la ventana principal.
	 */
	private void arregloLayout() {
		FormData formData = null;

		// Separacion vertical entre arbol y grafico.
		formData = new FormData();
		formData.top = new FormAttachment(this.toolBar);
		formData.bottom = new FormAttachment(this.lblStatus);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		this.sashForm.setLayoutData(formData);
		this.mostrarArbol(false);

		formData = new FormData();
		formData.left = new FormAttachment(0);
		formData.right = new FormAttachment(100);
		formData.bottom = new FormAttachment(100);
		this.lblStatus.setLayoutData(formData);
	}

	/**
	 * Inicializa el canvas donde se dibuja el diagrama.
	 */
	private void initFigureCanvas() {
		this.figureCanvas = new FigureCanvas(this.sashForm, SWT.V_SCROLL | SWT.H_SCROLL);
		this.figureCanvas.setHorizontalScrollBarVisibility(FigureCanvas.AUTOMATIC);
		this.figureCanvas.setVerticalScrollBarVisibility(FigureCanvas.AUTOMATIC);
		this.figureCanvas.setBackground(Principal.defaultBackgroundColor);
		this.figureCanvas.getViewport().setContentsTracksHeight(true);
		this.figureCanvas.getViewport().setContentsTracksWidth(true);
	}

	/**
	 * Crea un nuevo proyecto.
	 * 
	 * @throws Exception
	 */
	public void nuevoProyecto() {
		int resultadoGuardar = this.preguntarGuardar();

		if (resultadoGuardar != SWT.CANCEL) {
			PromptResult resultado = DialogBuilder.prompt(this.shell, "Ingresar nombre", "Nombre");

			if (resultado.result == Resultado.OK) {
				this.proyecto = new Proyecto(resultado.value);
				this.cargarProyecto();
			}
		}
	}

	/**
	 * Abre un proyecto.
	 */
	public void abrirProyecto() {
		int resultado = this.preguntarGuardar();

		if (resultado != SWT.CANCEL) {
			FileDialog fileDialog = new FileDialog(this.shell);
			fileDialog.setFilterExtensions(extensionProyecto);
			String path = fileDialog.open();

			if (path != null) {
				try {
					SaverLoaderXML modelo = new SaverLoaderXML(path);
					this.proyecto = modelo.load();
					this.cargarProyecto();
				} catch (Exception e) {
					e.printStackTrace();
					error(e.getMessage());
				}
			}
		}
	}

	/**
	 * Carga el proyecto actual.
	 */
	private void cargarProyecto() {
		this.proyecto.setDiagramaActual(this.proyecto.getDiagramaRaiz().getId());
		this.panelDiagramaLogico = new DiagramaLogicoFigura(this.figureCanvas, this.proyecto);
		this.panelDiagrama = new DiagramaFigura(this.figureCanvas, this.proyecto);
		this.panelDiagrama.actualizar();

		// Carga inicial del arbol.
		TreeManager.cargar(this.proyecto);
		this.mostrarArbol(true);
		// Notificar a la toolbar que hay un proyecto abierto.
		this.setChanged();
		this.notifyObservers();

		this.modificado(false);
		this.actualizarEstado();
	}

	private void cargarInterfazLogica() {
		this.panelDiagramaLogico = new DiagramaLogicoFigura(this.figureCanvas, this.proyecto);
		this.panelDiagramaLogico.actualizar();
	}

	private void cargarInterfazNormal() {
		this.panelDiagrama = new DiagramaFigura(this.figureCanvas, this.proyecto);
		this.panelDiagrama.actualizar();
	}

	/**
	 * Actualiza la barra de estado con el del proyecto y el diagrama actual.
	 */
	private void actualizarEstado() {
		String status = "[Ningun proyecto abierto]";

		if (this.proyecto != null) {
			status = "Proyecto: %s [%s]- Diagrama: %s [%s]";
			status = String.format(status, this.proyecto.getNombre(), this.proyecto.getValidacion().getEstado()
					.toString(), this.proyecto.getDiagramaActual().getNombre(), this.proyecto.getDiagramaActual()
					.getValidacion().getEstado().toString());
		}

		this.lblStatus.setText(status);
	}

	/**
	 * Actualiza el titulo dependiendo de si el proyecto tiene modificaciones
	 * que todavåƒ˜ no se guardaron.
	 */
	private void actualizarTitulo() {
		String titulo = APP_NOMBRE;

		if (this.proyecto != null) {
			titulo += " - " + this.proyecto.getNombre();
			titulo += this.shell.getModified() ? " *" : "";
			titulo += " [" + this.proyecto.getPath() + "]";
		}

		this.shell.setText(titulo);
	}

	/**
	 * Guarda un proyecto en el path que ya tiene asignado o muestra el dialogo
	 * para elegir el archivo.
	 */
	public void guardarProyecto() {
		this.guardarProyecto(false);
	}

	/**
	 * Guarda un proyecto en el path indicado.
	 * 
	 * @param showDialog
	 *            indica si se debe mostrar el dialogo de seleccion de archivo.
	 */
	public void guardarProyecto(boolean showDialog) {
		String path = this.proyecto.getPath();

		if (path == null || showDialog) {
			FileDialog fileDialog = new FileDialog(this.shell, SWT.SAVE);
			fileDialog.setFilterExtensions(extensionProyecto);
			path = fileDialog.open();
		}

		if (path != null) {
			File file = new File(path);
			String dir = file.getParent() + File.separator;
			this.proyecto.setPath(path);
			SaverLoaderXML modelo;
			try {
				modelo = new SaverLoaderXML(this.proyecto);
				this.guardarXml(modelo.saveProyecto(), path);

				this.guardarXml(modelo.saveComponentes(), dir + this.proyecto.getComponentesPath());

				this.guardarXml(modelo.saveRepresentacion(), dir + this.proyecto.getRepresentacionPath());

				this.guardarXml(modelo.saveComponentesLogicos(), dir + this.proyecto.getComponentesLogicosPath());

				this.guardarXml(modelo.saveRepresentacionDER(), dir + this.proyecto.getRepresentacionDERPath());

			} catch (Exception e) {
				this.error("Ocurrio un error al guardar el proyecto.");
				e.printStackTrace();
			}

			this.modificado(false);
		}
	}

	/**
	 * Guarda un objecto Document en un archivo fisico en el path especificado.
	 * 
	 * @param doc
	 * @param path
	 * @throws Exception
	 */
	private void guardarXml(Document doc, String path) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		// Indicar que escriba el xml con indentaciî‰¢.
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(path));
		transformer.transform(source, result);
	}

	/**
	 * Agrega un Diagrama al proyecto.
	 */
	public void agregarDiagrama() {
		PromptResult resultado = DialogBuilder.prompt(this.shell, "Ingresar nombre", "Nombre");
		if (resultado.result == Resultado.OK) {
			DiagramaControl nuevoDiagrama = new DiagramaControl(this.proyecto);
			nuevoDiagrama.setNombre(resultado.value);

			this.proyecto.agregar(nuevoDiagrama);
			this.actualizarVista();

			TreeManager.agregarADiagramaActual(nuevoDiagrama);

			this.modificado(true);
		}
	}

	/**
	 * Actualiza la vista.
	 */
	public void actualizarVista() {
		this.panelDiagrama.actualizar();
	}

	public void actualizarVistaLogica() {
		this.panelDiagramaLogico.actualizar();

	}

	/**
	 * Cierra el programa.
	 */
	public void salir() {
		System.exit(0);
	}

	/**
	 * Abre el diagrama para su visualizacion y/o edicion
	 * 
	 * @param id
	 *            Identificador del diagrama a abrir.
	 **/
	public void abrirDiagrama(String id) {
		String idActual = this.proyecto.getDiagramaActual().getId();
		cargarInterfazNormal();
		this.panelDiagrama.setVisible(true);
		this.panelDiagramaLogico.setVisible(false);

		if (!id.equals(idActual)) {
			int resultado = this.preguntarGuardar();

			if (resultado != SWT.CANCEL) {
				this.proyecto.setDiagramaActual(id);
				this.actualizarVista();
				this.actualizarEstado();
			}
		}
	}

	public void abrirDiagramaLogico(String id) {
		this.panelDiagrama.setVisible(false);
		this.panelDiagramaLogico.setVisible(true);
		this.proyecto.setDiagramaLogico(id);
		this.cargarInterfazLogica();
		this.actualizarVistaLogica();
		this.panelDiagramaLogico.actualizar();
	}

	/**
	 * Pregunta al usuario si se quiere guardar el diagrama. Si se ingresa un
	 * si, se realiza el guardado del diagrama y se devuelve el resultado del
	 * dialogo.
	 * 
	 * @return resultado: SWT.YES | SWT.NO | SWT.CANCEL
	 */
	private int preguntarGuardar() {
		int result = SWT.NO;

		if (shell.getModified()) {
			int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.CANCEL;
			MessageBox messageBox = new MessageBox(shell, style);
			messageBox.setText(TITULO_GUARDAR_DIAGRAMA_ACTUAL);
			messageBox.setMessage(MENSAJE_GUARDAR_DIAGRAMA_ACTUAL);

			result = messageBox.open();

			if (result == SWT.YES)
				guardarProyecto();
		}

		return result;
	}

	/**
	 * Abre el dialogo para agregar una Entidad al diagrama actual.
	 */
	public void agregarEntidad() {
		AgregarEntidadDialog dialog = new AgregarEntidadDialog();
		if (dialog.open() == Window.OK) {
			this.proyecto.agregar(dialog.getComponente());
			this.actualizarVista();
			TreeManager.agregarADiagramaActual(dialog.getComponente());
			this.modificado(true);
		}
	}

	/**
	 * Abre el dialogo para agregar una Relacion al diagrama actual.
	 */
	public void agregarRelacion() {
		AgregarRelacionDialog dialog = new AgregarRelacionDialog();
		if (dialog.open() == Window.OK) {
			this.proyecto.agregar(dialog.getComponente());
			this.actualizarVista();
			TreeManager.agregarADiagramaActual(dialog.getComponente());
			this.modificado(true);
		}
	}

	/**
	 * Abre el dialogo para agregar una Jerarquia al diagrama actual.
	 */
	public void agregarJerarquia() {
		AgregarJerarquiaDialog dialog = new AgregarJerarquiaDialog();
		if (dialog.open() == Window.OK) {
			this.proyecto.agregar(dialog.getComponente());
			this.actualizarVista();
			TreeManager.agregarADiagramaActual(dialog.getComponente());
			this.modificado(true);
		}
	}

	/**
	 * Disminuciî‰¢ del zoom.
	 */
	public void zoomOut(Combo cboZoom) {
		this.panelDiagrama.zoomOut();
		cboZoom.setText(this.panelDiagrama.getZoom());
	}

	/**
	 * Aumento del zoom.
	 */
	public void zoomIn(Combo cboZoom) {
		this.panelDiagrama.zoomIn();
		cboZoom.setText(this.panelDiagrama.getZoom());
	}

	/**
	 * Establece un valor zoom determinado.
	 * 
	 * @param zoom
	 *            Debe ser alguno de los valores establecidos en
	 *            {@link DiagramaFigura#zoomOptions}.
	 */
	public void zoom(String zoom) {
		this.panelDiagrama.zoom(zoom);
	}

	/**
	 * Exportar el diagrama a un archivo de imagen.
	 */
	public void exportar() {
		FileDialog fileDialog = new FileDialog(this.shell, SWT.SAVE);
		fileDialog.setFilterExtensions(extensionesImagen);
		fileDialog.setFileName(this.proyecto.getDiagramaActual().getNombre() + ".jpg");
		String path = fileDialog.open();

		if (path != null) {
			Image image = this.panelDiagrama.getImagen();

			ImageData[] data = new ImageData[1];
			data[0] = image.getImageData();

			ImageLoader imgLoader = new ImageLoader();
			imgLoader.data = data;
			imgLoader.save(path, SWT.IMAGE_JPEG);
		}

		FileDialog fileDialog2 = new FileDialog(this.shell, SWT.SAVE);
		fileDialog2.setFilterExtensions(extensionesImagen);
		fileDialog2.setFileName("Diagrama_Logico" + ".jpg");
		String path2 = fileDialog2.open();

		if (path2 != null) {
			Image image2 = this.panelDiagrama.getImagen();

			ImageData[] data2 = new ImageData[1];
			data2[0] = image2.getImageData();

			ImageLoader imgLoader2 = new ImageLoader();
			imgLoader2.data = data2;
			imgLoader2.save(path2, SWT.IMAGE_JPEG);
		}
	}

	/**
	 * Muestra la pantalla de impresiî‰¢ para el digrama actual.
	 */
	public void imprimir() {
		PrintDialog printDialog = new PrintDialog(this.shell);
		PrinterData printerData = printDialog.open();

		if (printerData != null) {
			Printer printer = new Printer(printerData);

			PrintFigureOperation printerOperation = new PrintFigureOperation(printer, this.panelDiagrama);
			printerOperation.setPrintMode(PrintFigureOperation.FIT_PAGE);
			printerOperation.setPrintMargin(new Insets(0, 0, 0, 0));
			printerOperation.run(this.proyecto.getDiagramaActual().getNombre());

			printer.dispose();
		}
	}

	/**
	 * Validar diagrama actual.
	 */
	public void validar() {
		Observacion observacion = this.proyecto.getDiagramaActual().validar();
		this.actualizarEstado();

		if (observacion.isEmpty())
			this.advertencia(Observacion.SIN_OBSERVACIONES);
		else {
			this.advertencia(observacion.toString());

			String nombreArchivo = "Diagrama-" + this.proyecto.getDiagramaActual().getNombre();
			nombreArchivo += String.format("-%s.txt", dateFormat.format(new Date()));

			this.guardarValidacion(nombreArchivo, observacion.toString());
		}
	}

	/**
	 * Validar proyecto.
	 */
	public void validarProyecto() {
		Observacion observacion = this.proyecto.validar();
		this.actualizarEstado();

		if (observacion.isEmpty())
			this.advertencia(Observacion.SIN_OBSERVACIONES);
		else {
			this.advertencia(observacion.toString());

			String nombreArchivo = "Proyecto-" + this.proyecto.getDiagramaRaiz().getNombre();
			nombreArchivo += String.format("_%s.txt", dateFormat.format(new Date()));

			this.guardarValidacion(nombreArchivo, observacion.toString());
		}
	}

	/**
	 * Muestra una ventana de diç–�ogo para seleccionar donde guardar el
	 * resultado de la validacion.
	 * 
	 * @param nombreArchivo
	 *            Nombre por defecto del archivo de validacion.
	 * @param resultado
	 *            Resultado de la validaciî‰¢.
	 */
	private void guardarValidacion(String path, String resultado) {
		FileDialog fileDialog = new FileDialog(this.shell, SWT.SAVE);
		fileDialog.setFileName(path);
		fileDialog.setFilterExtensions(extensionValidacion);
		path = fileDialog.open();

		if (path != null) {
			try {

				File file = new File(path);
				Writer output = new BufferedWriter(new FileWriter(file));
				output.write(resultado);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
				error(e.getMessage());
			}
		}
	}

	/**
	 * Implementaciî‰¢ de la interfaz FigureListener por medio de la cual la
	 * aplicaciî‰¢ se entera cuando se mueve alguna de las figuras.
	 * 
	 * @param source
	 */
	@Override
	public void figureMoved(IFigure source) {
		this.modificado(true);
	}

	/**
	 * Devuelve el shel de la ventana principal.
	 * 
	 * @return
	 */
	public Shell getShell() {
		return this.shell;
	}

	/**
	 * Devuelve un proxy del proyecto que se encuentra abierto exponiendo solo
	 * los mé¨�odos de la interfaz <code>ProyectoProxy</code>.
	 * 
	 * @return
	 */
	public ProyectoProxy getProyecto() {
		return this.proyecto;
	}

	/**
	 * Muestra una ventana de error con el mensaje especificado.
	 * 
	 * @param mensaje
	 */
	public void error(String mensaje) {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		messageBox.setText("Error");
		messageBox.setMessage(mensaje != null ? mensaje : "Ocurriï¿½un error");
		messageBox.open();
	}

	/**
	 * Muestra una ventana de advertencia con el mensaje especificado.
	 * 
	 * @param mensaje
	 */
	public void advertencia(String mensaje) {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
		messageBox.setText("Advertencia");
		messageBox.setMessage(mensaje);
		messageBox.open();
	}

	/**
	 * Define si el proyecto fue modificado y actualiza el titulo de la ventana
	 * principal.
	 * 
	 * @param modificado
	 *            Define el estado del diagrama actual. <code>true</code> si el
	 *            diagrama tiene alguna modificaciî‰¢ pendiente de ser guardada.
	 *            <code>false</code> si no tiene ninguna.
	 */
	private void modificado(boolean modificado) {
		if (modificado != this.shell.getModified()) {
			this.shell.setModified(modificado);
			this.actualizarTitulo();
		}

		if (modificado && this.proyecto != null) {
			this.proyecto.getValidacion().setEstado(EstadoValidacion.SIN_VALIDAR);
			this.proyecto.getDiagramaActual().getValidacion().setEstado(EstadoValidacion.SIN_VALIDAR);

			this.actualizarEstado();
		}
	}

	/**
	 * Muestra o esconde el arbol de jerarquåƒ˜s segä½– el valor del parç–¥etros
	 * 
	 * @param mostrar
	 *            indica si se debe mostrar el ç–µbol.
	 */
	public void mostrarArbol(boolean mostrar) {
		int peso = mostrar ? 3 : 0;
		this.sashForm.setWeights(new int[] { peso, 16 });
	}

	/**
	 * Convierte de DER a logico
	 */
	public void convertir() {
		ConversorDERaLogico conversor = ConversorDERaLogico.getInstance();
		DiagramaLogico diaLog = conversor.convertir(this.proyecto.getDiagramaActual());
		DiagramaLogicoControl diaControl = new DiagramaLogicoControl(diaLog);
		this.proyecto.setDiagramaLogico(diaControl);

		ConversorDERRepresentacion converRep = new ConversorDERRepresentacion();

		converRep.createRepresentation(this.proyecto.getDiagramaLogicoControl());

		TreeManager.agregarADiagramaActual(diaControl);
		this.modificado(true);
	}

}
