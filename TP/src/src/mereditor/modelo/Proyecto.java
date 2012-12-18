package mereditor.modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mereditor.control.DiagramaDERControl;
import mereditor.control.DiagramaLogicoControl;
import mereditor.interfaz.swt.figuras.Figura;
import mereditor.modelo.Validacion.EstadoValidacion;
import mereditor.modelo.base.Componente;
import mereditor.modelo.base.ComponenteNombre;
import mereditor.modelo.validacion.Observacion;
import mereditor.modelo.validacion.ValidarEquilibrioAtributos;
import mereditor.modelo.validacion.ValidarEquilibrioComponentes;
import mereditor.modelo.validacion.ValidarEquilibrioRelaciones;
import mreleditor.modelo.DiagramaLogico;

public class Proyecto extends ComponenteNombre implements ProyectoProxy {
	/**
	 * DiagramaDER raiz del proyecto.
	 */
	protected DiagramaDER raiz;
	/**
	 * DiagramaDER que se encuetra abierto.
	 */
	protected Diagrama diagramaActual;
	/**
	 * Validación del proyecto entero.
	 */
	protected Validacion validacion;
	/**
	 * Path al archivo donde se guardo por ultima vez el proyecto abierto.
	 */
	protected String path;

	/**
	 * Mapa de todos los componentes presentes en el proyecto.
	 */
	protected Map<String, Componente> componentes = new HashMap<>();

	/**
	 * Constructor para crear un nuevo proyecto.
	 * 
	 * @throws Exception
	 */
	public Proyecto() {
		this.validacion = new Validacion();
	}
	
	protected DiagramaLogico diagramaLogico;
	@SuppressWarnings("rawtypes")
	protected ArrayList<Figura> listaObjetosLogicos;

	/**
	 * Constructor para crear un proyecto con un diagrama con nombre.
	 * 
	 * @param nombre
	 * @throws Exception
	 */
	public Proyecto(String nombre) {
		this();
		this.raiz = new DiagramaDERControl(this);
		this.raiz.setNombre(nombre);
		this.agregar(this.raiz);
	}
	
	@SuppressWarnings("rawtypes")
	public void setListaObjetosLogicos(ArrayList<Figura> lista) {
		this.listaObjetosLogicos = lista;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList<Figura> getListaObjetosLogicos() {
		return this.listaObjetosLogicos;
	}
	
	/**
	 * Obtener diagrama raiz del proyecto.
	 * 
	 * @return
	 */
	public DiagramaDER getDiagramaRaiz() {
		return raiz;
	}

	/**
	 * Establece el diagrama raíz del proyecto.
	 * 
	 * @return
	 */
	public void setDiagramaRaiz(DiagramaDER raiz) {
		if (this.raiz != null)
			throw new RuntimeException("El diagrama raiz ya esta establecido.");

		this.raiz = (DiagramaDERControl) raiz;
		if (!this.componentes.containsKey(raiz.getId()))
			this.agregar(raiz);
	}

	/**
	 * Devuelve la validación de todo el proyecto.
	 * 
	 * @return
	 */
	public Validacion getValidacion() {
		return this.validacion;
	}

	/**
	 * Establece la validación de todo el proyecto.
	 * 
	 * @param validacion
	 */
	public void setValidacion(Validacion validacion) {
		this.validacion = validacion;
	}

	/**
	 * Establece el diagrama actual
	 * 
	 * @param id
	 */
	public void setDiagramaActual(String id) {
		if (this.componentes.containsKey(id)) {
			Componente diagrama = this.componentes.get(id);
			if (DiagramaDERControl.class.isInstance(diagrama) ||
					DiagramaLogicoControl.class.isInstance(diagrama))
				this.diagramaActual = (Diagrama) diagrama;
		}
	}

	/**
	 * Devuelve el diagrama actual.
	 * 
	 * @return
	 */
	public Diagrama getDiagramaActual() {
		return this.diagramaActual;
	}
	

	/**
	 * Agregar un componente nuevo al proyecto y al diagrama actual si es que
	 * hay uno establecido.
	 * 
	 * @param componente
	 */
	public void agregar(Componente componente) {
		if (!this.componentes.containsKey(componente.getId()))
			this.componentes.put(componente.getId(), componente);

		if (this.diagramaActual != null)
			this.diagramaActual.agregar(componente);
	}
	
	/**
	 * Elimina el componente de la colección de componentes.
	 * @param componente
	 */
	public void eliminar(Componente componente) {
		if(componente.getAllPadres().size() == 0)
			this.componentes.remove(componente.getId());
	}
	
	/**
	 * Devuelve el componente que tiene el id.
	 * 
	 * @param id
	 * @return
	 */
	public Componente getComponente(String id) {
		return this.componentes.get(id);
	}

	/**
	 * Devuelve la coleccion de componentes.
	 * 
	 * @return
	 */
	@Override
	public Collection<Componente> getComponentes() {
		return Collections.unmodifiableCollection(this.componentes.values());
	}

	/**
	 * Indica si el proyecto tiene el componente con el id indicado.
	 * 
	 * @param id
	 * @return
	 */
	public boolean contiene(String id) {
		return this.componentes.containsKey(id);
	}

	/**
	 * Establecer el path donde se debe guardar el proyecto.
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Devuelve el path donde se guardó el archivo
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Obtener el path del archivo de componentes.
	 * 
	 * @return
	 */
	public String getComponentesPath() {
		File file = new File(path);
		String nombre = file.getName().replaceFirst("[.][^.]+$", "");
		return nombre + "-comp.xml";
	}

	/**
	 * Obtener el path del archivo de representacion.
	 * 
	 * @return
	 */
	public String getRepresentacionPath() {
		File file = new File(path);
		String nombre = file.getName().replaceFirst("[.][^.]+$", "");
		return nombre + "-rep.xml";
	}

	/**
	 * Obtener el conjunto de entidades.
	 * 
	 * @return
	 */
	public Set<Entidad> getEntidades() {
		return Componente.filtrarComponentes(Entidad.class,
				this.componentes.values());
	}

	/**
	 * Obtener el conjunto de relaciones.
	 * 
	 * @return
	 */
	public Set<Relacion> getRelaciones() {
		return Componente.filtrarComponentes(Relacion.class,
				this.componentes.values());
	}

	/**
	 * Obtener el conjunto de jerarquias.
	 * 
	 * @return
	 */
	public Set<Jerarquia> getJerarquias() {
		return Componente.filtrarComponentes(Jerarquia.class,
				this.componentes.values());
	}

	/**
	 * Obtener el conjunto de diagramas.
	 * 
	 * @return
	 */
	public Set<DiagramaDER> getDiagramas() {
		return Componente.filtrarComponentes(DiagramaDER.class,
				this.componentes.values());
	}

	/**
	 * Toma como nombre de este proyecto el del diagrama raíz.
	 */
	public String getNombre() {
		return this.raiz.getNombre();
	}

	@Override
	public Set<Entidad> getEntidadesDisponibles() {
		if(!DiagramaDER.class.isInstance(diagramaActual))
			return new HashSet<Entidad>();
		// Obtener las entidades de los ancestros
		return ((DiagramaDER)this.diagramaActual).getEntidades(true);
	}

	@Override
	public Set<Entidad> getEntidadesDiagrama() {
		if(!DiagramaDER.class.isInstance(diagramaActual))
			return new HashSet<Entidad>();
		return ((DiagramaDER)this.diagramaActual).getEntidades(false);
	}

	@Override
	public Collection<Atributo> getAtributosDiagrama() {
		if(!DiagramaDER.class.isInstance(diagramaActual))
			return new HashSet<Atributo>();
		return ((DiagramaDER)this.diagramaActual).getAtributos(false);
	}

	@Override
	public Set<Relacion> getRelacionesDisponibles() {
		if(!DiagramaDER.class.isInstance(diagramaActual))
			return new HashSet<Relacion>();
		return ((DiagramaDER)this.diagramaActual).getRelaciones(true);
	}

	@Override
	public Set<Relacion> getRelacionesDiagrama() {
		if(!DiagramaDER.class.isInstance(diagramaActual))
			return new HashSet<Relacion>();
		return ((DiagramaDER)this.diagramaActual).getRelaciones(false);
	}

	@Override
	public Set<Jerarquia> getJerarquiasDisponibles() {
		if(!DiagramaDER.class.isInstance(diagramaActual))
			return new HashSet<Jerarquia>();
		return ((DiagramaDER)this.diagramaActual).getJerarquias(true);
	}

	@Override
	public Set<Jerarquia> getJerarquiasDiagrama() {
		if(!DiagramaDER.class.isInstance(diagramaActual))
			return new HashSet<Jerarquia>();
		return ((DiagramaDER)this.diagramaActual).getJerarquias(false);
	}

	@Override
	public void addValidaciones() {
		this.validaciones.add(new ValidarEquilibrioComponentes());
		this.validaciones.add(new ValidarEquilibrioAtributos());
		this.validaciones.add(new ValidarEquilibrioRelaciones());
	}

	@Override
	public Observacion validar() {
		Observacion observacion = super.validar();
		
		for (DiagramaDER diagrama : this.getDiagramas())
			observacion.addObservacion(diagrama.validar());

		if (observacion.isEmpty())
			this.validacion.setEstado(EstadoValidacion.VALIDADO);
		else {
			this.validacion.setObservaciones(observacion.toString());
			this.validacion.setEstado(EstadoValidacion.VALIDADO_CON_OBSERVACIONES);
		}			

		return observacion;
	}
	
	@Override
	public String toString() {
		return this.getNombre();
	}
}
