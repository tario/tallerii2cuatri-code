package mereditor.interfaz.swt.figuras;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.Graphics;

import mereditor.modelo.Atributo;
import mereditor.modelo.Entidad;
import mereditor.modelo.Jerarquia;
import mereditor.modelo.Relacion;
import mereditor.modelo.Atributo.TipoAtributo;
import mereditor.modelo.Entidad.TipoEntidad;
import mereditor.modelo.Jerarquia.TipoJerarquia;
import mereditor.modelo.Relacion.TipoRelacion;
import mereditor.representacion.PList;

public class EstilosFiguras {
	private static Map<Class<?>, Map<Object, PList>> estilos = new HashMap<>();
	
	private static final PList negro = new PList().set("r", 0).set("g", 0).set("b", 0);
	private static final PList blanco = new PList().set("r", 255).set("g", 255).set("b", 255);
	private static final PList crema = new PList().set("r", 255).set("g", 255).set("b", 206);

	static {
		estilos.put(Entidad.class, new HashMap<Object, PList>());
		estilos.put(Relacion.class, new HashMap<Object, PList>());
		estilos.put(Atributo.class, new HashMap<Object, PList>());
		estilos.put(Jerarquia.class, new HashMap<Object, PList>());
		
		estilos.get(Entidad.class).put(TipoEntidad.MAESTRA_COSA, new PList());
		estilos.get(Entidad.class).put(TipoEntidad.MAESTRA_DOMINIO, new PList());
		estilos.get(Entidad.class).put(TipoEntidad.TRANSACCIONAL_HISTORICA, new PList());
		estilos.get(Entidad.class).put(TipoEntidad.TRANSACCIONAL_PROGRAMADA, new PList());
		
		estilos.get(Relacion.class).put(TipoRelacion.ASOCIACION, new PList());
		estilos.get(Relacion.class).put(TipoRelacion.COMPOSICION, new PList());
		
		estilos.get(Atributo.class).put(TipoAtributo.CARACTERIZACION, new PList());
		estilos.get(Atributo.class).put(TipoAtributo.DERIVADO_CALCULO, new PList());
		estilos.get(Atributo.class).put(TipoAtributo.DERIVADO_COPIA, new PList());
		
		estilos.get(Jerarquia.class).put(TipoJerarquia.PARCIAL_EXCLUSIVA, new PList());
		estilos.get(Jerarquia.class).put(TipoJerarquia.PARCIAL_SUPERPUESTA, new PList());
		estilos.get(Jerarquia.class).put(TipoJerarquia.TOTAL_EXCLUSIVA, new PList());
		estilos.get(Jerarquia.class).put(TipoJerarquia.TOTAL_SUPERPUESTA, new PList());
		
		PList rep = null;
		/**
		 * Entidad
		 */
		rep = estilos.get(Entidad.class).get(TipoEntidad.MAESTRA_COSA);
		rep.set("ColorFondo", crema);
		rep.set("ColorLinea", negro);

		rep = estilos.get(Entidad.class).get(TipoEntidad.MAESTRA_DOMINIO);
		rep.set("ColorFondo", crema);
		rep.set("ColorLinea", negro);

		rep = estilos.get(Entidad.class).get(TipoEntidad.TRANSACCIONAL_HISTORICA);
		rep.set("ColorFondo", crema);
		rep.set("ColorLinea", negro);

		rep = estilos.get(Entidad.class).get(TipoEntidad.TRANSACCIONAL_PROGRAMADA);
		rep.set("ColorFondo", crema);
		rep.set("ColorLinea", negro);
		
		/**
		 * Relacion
		 */
		rep = estilos.get(Relacion.class).get(TipoRelacion.ASOCIACION);
		rep.set("ColorFondo", crema);
		rep.set("ColorLinea", negro);

		rep = estilos.get(Relacion.class).get(TipoRelacion.COMPOSICION);
		rep.set("ColorFondo", crema);
		rep.set("ColorLinea", negro);
		
		/**
		 * Atributo
		 */
		rep = estilos.get(Atributo.class).get(TipoAtributo.CARACTERIZACION);
		rep.set("ColorFondo", blanco);
		rep.set("ColorLinea", negro);
		rep.set("EstiloLinea", Graphics.LINE_SOLID);

		rep = estilos.get(Atributo.class).get(TipoAtributo.DERIVADO_CALCULO);
		rep.set("ColorFondo", blanco);
		rep.set("ColorLinea", negro);
		rep.set("EstiloLinea", Graphics.LINE_SOLID);

		rep = estilos.get(Atributo.class).get(TipoAtributo.DERIVADO_COPIA);
		rep.set("ColorFondo", blanco);
		rep.set("ColorLinea", negro);
		rep.set("EstiloLinea", Graphics.LINE_DOT);
		
		/**
		 * Jerarquia
		 */
		rep = estilos.get(Jerarquia.class).get(TipoJerarquia.PARCIAL_EXCLUSIVA);
		rep.set("ColorFondo", crema);
		rep.set("ColorLinea", negro);

		rep = estilos.get(Jerarquia.class).get(TipoJerarquia.PARCIAL_SUPERPUESTA);
		rep.set("ColorFondo", crema);
		rep.set("ColorLinea", negro);

		rep = estilos.get(Jerarquia.class).get(TipoJerarquia.TOTAL_EXCLUSIVA);
		rep.set("ColorFondo", crema);
		rep.set("ColorLinea", negro);

		rep = estilos.get(Jerarquia.class).get(TipoJerarquia.TOTAL_SUPERPUESTA);
		rep.set("ColorFondo", crema);
		rep.set("ColorLinea", negro);
	}
	
	public static PList get(Class<?> clazz, Object tipo) {
		return estilos.get(clazz).get(tipo);
	}
}
