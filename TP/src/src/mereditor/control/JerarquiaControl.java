package mereditor.control;

import java.util.HashMap;
import java.util.Map;

import mereditor.interfaz.swt.editores.EditorFactory;
import mereditor.interfaz.swt.figuras.Figura;
import mereditor.interfaz.swt.figuras.JerarquiaFigura;
import mereditor.modelo.Diagrama;
import mereditor.modelo.Entidad;
import mereditor.modelo.Jerarquia;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;

public class JerarquiaControl extends Jerarquia implements Control<Jerarquia>,
		MouseListener {
	protected Map<String, JerarquiaFigura> figures = new HashMap<>();

	@Override
	public Figura<Jerarquia> getFigura(String idDiagrama) {
		if (!this.figures.containsKey(idDiagrama)) {
			JerarquiaFigura figura = new JerarquiaFigura(this);
			this.figures.put(idDiagrama, figura);
		}

		this.figures.get(idDiagrama).actualizar();

		return this.figures.get(idDiagrama);
	}

	@Override
	public void dibujar(Figure contenedor, String idDiagrama) {
		// Obtener el diagrama padre correspondiente
		Diagrama padre = (Diagrama) this.getPadre(idDiagrama);
		// Dibujar solo si la generica está en el diagrama
		if (padre.contiene(this.generica)) {
			JerarquiaFigura figJerarquia = (JerarquiaFigura) this
					.getFigura(idDiagrama);
			contenedor.add(figJerarquia);

			Figure figGenerica = ((Control<?>) this.generica)
					.getFigura(idDiagrama);
			figJerarquia.conectarGenerica(figGenerica);

			for (Entidad derivada : this.derivadas) {
				// Dibujar la conexión sólo si la derivada pertenece al diagrama
				if (padre.contiene(derivada)) {
					Figure figuraDerivada = ((Control<?>) derivada)
							.getFigura(idDiagrama);
					figJerarquia.conectarDerivada(figuraDerivada);
				}
			}
		}
	}

	@Override
	public String getNombreIcono() {
		return "jerarquia.png";
	}

	@Override
	public void mouseDoubleClicked(MouseEvent arg0) {
		EditorFactory.getEditor(this).open();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
