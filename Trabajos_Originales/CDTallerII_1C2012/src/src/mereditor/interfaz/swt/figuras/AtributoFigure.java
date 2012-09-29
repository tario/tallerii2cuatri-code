package mereditor.interfaz.swt.figuras;

import mereditor.interfaz.swt.listeners.ArrastreControlador;
import mereditor.modelo.Atributo;
import mereditor.representacion.PList;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class AtributoFigure extends Figura<Atributo> {
	private Ellipse ellipse;
	private Color identificadorBackColor = new Color(null, 0, 0, 0);

	public AtributoFigure(Atributo componente) {
		super(componente);
		this.setRepresentacion(EstilosFiguras.get(Atributo.class,
				this.componente.getTipo()));
	}

	@Override
	protected void init() {
		this.lblName = new Label();
		this.lblName.setFont(this.getFont());
		this.lblName.setText(this.getTextoLabel());
		this.lblName.setBounds(this.lblName.getTextBounds().getTranslated(0, -10));

		this.ellipse = new Ellipse();
		this.ellipse.setOpaque(false);
		this.ellipse.setLocation(this.getLocation());
		this.ellipse.setAntialias(SWT.ON);
		this.ellipse.setBackgroundColor(this.backColor);
		this.applyLineStyle();

		// Si es un atributo compuesto representar con circulo
		if (this.componente.esCompuesto()) {
			ellipse.setLayoutManager(new BorderLayout());
			ellipse.setSize(this.lblName.getTextBounds().getSize()
					.getExpanded(20, 5));
			ellipse.add(this.lblName, BorderLayout.CENTER);
		} else {
			ellipse.setSize(new Dimension(10, 10));
			// Permitir que el label se mueva
			new ArrastreControlador(this.lblName);
			this.addFiguraLoqueada(this.lblName);
		}

		this.setBounds(ellipse.getBounds());
		this.add(ellipse);
	}

	@Override
	public void setParent(IFigure parent) {
		super.setParent(parent);

		if (this.getParent() != null && !this.componente.esCompuesto())
			this.getParent().add(this.lblName, 0);
	}
	
	@Override
	protected void applyBackgroundColor() {
		this.ellipse.setBackgroundColor(this.backColor);
	}
	
	@Override
	protected void applyLineStyle() {
		this.applyLineStyle(this.ellipse);
	}

	/**
	 * Devuelve el texto que se debe mostrar en el diagrama.
	 * 
	 * @return
	 */
	private String getTextoLabel() {
		String texto = this.componente.getNombre();
		String cardMin = this.componente.getCardinalidadMinima();
		String cardMax = this.componente.getCardinalidadMaxima();

		if (cardMin != "" && cardMax != "")
			if (!cardMin.equals("1") || !cardMax.equals("1"))
				texto += " (" + cardMin + ", " + cardMax + ")";

		return texto;
	}

	/**
	 * Conecta este atributo con uno de sus atributos hijos
	 * 
	 * @param figura
	 */
	public void conectarAtributo(Figura<Atributo> figura) {
		this.getParent().add(Figura.conectarEllipse(this, figura));
	}

	@Override
	public void setRepresentacion(PList repr) {
		super.setRepresentacion(repr);

		if (this.lblName != null && repr.get("Label") != null) {
			PList labelRepr = repr.<PList> get("Label");
			this.lblName.setLocation(new Point(labelRepr.<Integer> get("x"),
					labelRepr.<Integer> get("y")));
		}
	}

	@Override
	public PList getRepresentacion() {
		PList repr = super.getRepresentacion();
		if (this.lblName != null) {
			PList labelRepr = new PList();
			repr.set("Label", labelRepr);
			labelRepr.set("x", this.lblName.getLocation().x);
			labelRepr.set("y", this.lblName.getLocation().y);
		}

		return repr;
	}

	@Override
	public void actualizar() {
		// Actualizar el estilo en caso de que haya cambiado el tipo del
		// componente.
		this.setRepresentacion(EstilosFiguras.get(Atributo.class,
				this.componente.getTipo()));

		// Si es un identificador, usar fondo negro
		if (this.componente.esIdentificador())
			ellipse.setBackgroundColor(this.identificadorBackColor);

		// Actualizar el texto del label y su tamaño.
		this.lblName.setText(this.getTextoLabel());
		this.lblName.setBounds(this.lblName.getTextBounds());
	}
}
