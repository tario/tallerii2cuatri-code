package mereditor.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Figure;

import mereditor.interfaz.swt.figuras.EntidadFigure;
import mereditor.interfaz.swt.figuras.Figura;
import mereditor.modelo.Atributo;
import mereditor.modelo.Entidad;
import mereditor.modelo.Proyecto;
import mereditor.modelo.Relacion;
import mereditor.modelo.Entidad.Identificador;
import mreleditor.modelo.Diagrama;
import mreleditor.modelo.Tabla;

public class DiagramaLogicoControl extends Diagrama implements Control<Diagrama>{

	public DiagramaLogicoControl(Proyecto proyecto) {
		super(proyecto);
	}
	
	@Override
	public Figura<Diagrama> getFigura(String idDiagrama) {
		return null;
	}

	@Override
	public void dibujar(Figure contenedor, String idDiagrama) {
		idDiagrama = idDiagrama != null ? idDiagrama : this.id;

		ArrayList<Tabla> entidades = this.getTablas();
		this.dibujar(contenedor, idDiagrama, entidades);
		this.dibujar(contenedor, idDiagrama, this.getRelaciones());

		List<Identificador> identificadores = new ArrayList<>();
		for (Tabla entidad : entidades) {
			//identificadores.addAll(entidad.getClave_primaria());
			
		}
		this.dibujarIdentificadores(identificadores);
		
	}
	
	private void dibujar(Figure contenedor, String id, Collection<?> componentes) {
		for (Object componente : componentes)
			((Control<?>) componente).dibujar(contenedor, id);
	}
	
	public void dibujar(Figure contenedor) {
		this.dibujar(contenedor, this.id);
	}
	
	/**
	 * Se encarga de la logica de dibujar las conexiones entre los diferentes
	 * elementos de los <code>Identificador</code>es de las <code>Entidad</code>
	 * es que pertenecen a este <code>Diagrama</code>.
	 * 
	 * @param identificadores
	 *            Lista de todos los <code>Identificador</code>es de las
	 *            <code>Entidad</code>es del <code>Diagrama</code>.
	 */
	private void dibujarIdentificadores(List<Identificador> identificadores) {
		for (Identificador identificador : identificadores) {
			List<Connection> conexiones = new ArrayList<>();

			EntidadControl entidadCtrl = (EntidadControl) identificador.getEntidad();
			EntidadFigure figEntidad = (EntidadFigure) entidadCtrl.getFigura(this.id);

			// Internos y mixtos
			if (identificador.isInterno()) {
				// Agregar los conectores a los atributos
				for (Atributo atributo : identificador.getAtributos())
					conexiones.add(figEntidad.getConexion(atributo.getId()));
			}
			// Externos
			if (identificador.isExterno()) {
				// Recorrer las entidades del identificador
				for (Entidad entidadIdf : identificador.getEntidades()) {
					// Encontrar la relacion que comparten
					RelacionControl relacion = (RelacionControl) entidadCtrl.relacion(entidadIdf);
					if (relacion != null && this.contiene(relacion)) {
						Figura<Relacion> figRelacion = relacion.getFigura(this.getId());
						// Obtener el conector de la relacion con la entidad del
						// identificador
						Connection conexion = figRelacion.getConexion(entidadIdf.getId());
						// Unir el conector con la entidad que tiene el
						// identificador.
						conexiones.add(figEntidad.conectarEntidad(entidadIdf.getId(), conexion));
					}
				}
			}
			// Mixtos
			if (identificador.isMixto()) {
				// Agregar los conectores a los atributos (se repite código para
				// mayor claridad)
				for (Atributo atributo : identificador.getAtributos())
					conexiones.add(figEntidad.getConexion(atributo.getId()));
				// Agregar los conectores de las entidades con la relacion
				for (Entidad entidadIdf : identificador.getEntidades()) {
					RelacionControl relacion = (RelacionControl) entidadCtrl.relacion(entidadIdf);
					if (relacion != null && this.contiene(relacion)) {
						Figura<Relacion> figRelacion = relacion.getFigura(this.getId());
						conexiones.add(figRelacion.getConexion(entidadIdf.getId()));
					}
				}
			}

			figEntidad.conectarIdentificador(conexiones);
		}
	}

	@Override
	public String getNombreIcono() {
		return "diagrama.png";
	}

}
