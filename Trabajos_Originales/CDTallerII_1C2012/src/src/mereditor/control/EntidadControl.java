package mereditor.control;

import java.util.HashMap;
import java.util.Map;

import mereditor.interfaz.swt.editores.EditorFactory;
import mereditor.interfaz.swt.figuras.EntidadFigure;
import mereditor.interfaz.swt.figuras.Figura;
import mereditor.modelo.Atributo;
import mereditor.modelo.Entidad;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;

public class EntidadControl extends Entidad implements Control<Entidad>, MouseListener {
	protected Map<String, EntidadFigure> figures = new HashMap<>();

	@Override
	public Figura<Entidad> getFigura(String idDiagrama) {
		if (!this.figures.containsKey(idDiagrama)) {
			EntidadFigure figura = new EntidadFigure(this);
			this.figures.put(idDiagrama, figura);
			
			this.setPosicionInicial(figura);
		}
		
		this.figures.get(idDiagrama).actualizar();

		return this.figures.get(idDiagrama);
	}

	/**
	 * Establecer la posición cuando es una nueva figura.
	 * @param figura
	 */
	private void setPosicionInicial(Figure figura) {
		figura.setBounds(figura.getBounds().getTranslated(100, 100));		
	}

	@Override
	public void dibujar(Figure contenedor, String idDiagrama) {
		EntidadFigure figuraEntidad = (EntidadFigure) this.getFigura(idDiagrama);
		contenedor.add(figuraEntidad);

		/*
		 * Dibujar y conectar Atributos
		 */
		for (Atributo atributo : this.atributos) {
			AtributoControl atributoControl = (AtributoControl) atributo;

			figuraEntidad.conectarAtributo(atributoControl.getFigura(idDiagrama));
			atributoControl.dibujar(contenedor, idDiagrama);
			figuraEntidad.addFiguraLoqueada(atributoControl.getFigura(idDiagrama));
		}
	}

	public Map<String, EntidadFigure> getFiguras() {
		return this.figures;
	}
	
	@Override
	public String getNombreIcono() {
		return "entidad.png";
	}

	@Override
	public void mouseDoubleClicked(MouseEvent event) {
		EditorFactory.getEditor(this).open();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}
}
