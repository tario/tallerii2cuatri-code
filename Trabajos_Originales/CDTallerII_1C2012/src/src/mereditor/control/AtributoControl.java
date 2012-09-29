package mereditor.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mereditor.interfaz.swt.editores.EditorFactory;
import mereditor.interfaz.swt.figuras.AtributoFigure;
import mereditor.interfaz.swt.figuras.Figura;
import mereditor.modelo.Atributo;
import mereditor.modelo.base.ComponenteAtributos;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;

public class AtributoControl extends Atributo implements Control<Atributo>, MouseListener {
	protected Map<String, AtributoFigure> figures = new HashMap<>();

	@Override
	public Figura<Atributo> getFigura(String idDiagrama) {
		if (!this.figures.containsKey(idDiagrama)) {
			AtributoFigure figura = new AtributoFigure(this);
			this.figures.put(idDiagrama, figura);
			
			this.setPosicionInicial(figura, idDiagrama);
		}

		this.figures.get(idDiagrama).actualizar();

		return this.figures.get(idDiagrama);
	}
	
	/**
	 * Establecer la posición cuando es una nueva figura.
	 * @param figura
	 * @param idDiagrama
	 */
	private void setPosicionInicial(Figure figura, String idDiagrama) {
		// Posicionar el atributo relativo a la posición del padre según su
		// posición en la colección de atributos.
		Control<?> padre = (Control<?>) this.getPadre();
		Figure figPadre = padre.getFigura(idDiagrama);
		ComponenteAtributos compAtribs = (ComponenteAtributos) padre;
		List<Atributo> attrs = new ArrayList<>(compAtribs.getAtributos());

		int x = figPadre.getSize().width + 20;
		int y = attrs.indexOf(this) * 20;
		figura.setLocation(figPadre.getLocation().getTranslated(x, y));		
	}

	@Override
	public void dibujar(Figure contenedor, String idDiagrama) {
		AtributoFigure figura = (AtributoFigure) this.getFigura(idDiagrama);
		contenedor.add(figura);

		for (Atributo atributo : this.atributos) {
			AtributoControl atributoControl = (AtributoControl) atributo;
			figura.conectarAtributo(atributoControl.getFigura(idDiagrama));
			atributoControl.dibujar(contenedor, idDiagrama);

			figura.addFiguraLoqueada(atributoControl.getFigura(idDiagrama));
		}
	}

	public Map<String, AtributoFigure> getFiguras() {
		return this.figures;
	}

	@Override
	public String getNombreIcono() {
		return "atributo.png";
	}

	@Override
	public void mouseDoubleClicked(MouseEvent arg0) {
		EditorFactory.getEditor(this).open();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}