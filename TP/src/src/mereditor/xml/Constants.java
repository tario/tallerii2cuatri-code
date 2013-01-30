package mereditor.xml;

public class Constants {
	public static final String ID_ATTR = "id";
	public static final String TIPO_ATTR = "tipo";
	public static final String ESTADO_ATTR = "estado";
	public static final String IDREF_ATTR = "idref";
	public static final String CARDINALIDAD_MIN_ATTR = "min";
	public static final String CARDINALIDAD_MAX_ATTR = "max";
	public static final String X_ATTR = "x";
	public static final String Y_ATTR = "y";
	public static final String ANCHO_ATTR = "ancho";
	public static final String ALTO_ATTR = "alto";

	public static final String NOMBRE_TAG = "Nombre";

	public static final String ENTIDAD_TAG = "Entidad";
	public static final String ATRIBUTOS_TAG = "Atributos";
	public static final String ATRIBUTO_TAG = "Atributo";
	public static final String CARDINALIDAD_TAG = "Cardinalidad";
	public static final String FORMULA_TAG = "Formula";
	public static final String ORIGEN_TAG = "Origen";
	public static final String REFATRIBUTO_TAG = "RefAtributo";
	public static final String REFENTIDAD_TAG = "RefEntidad";
	public static final String RELACION_TAG = "Relacion";
	public static final String PARTICIPANTES_TAG = "Participantes";
	public static final String PARTICIPANTE_TAG = "Participante";
	public static final String ROL_TAG = "Rol";
	public static final String DIAGRAMA_TAG = "Diagrama";
	public static final String DIAGRAMAS_TAG = "Diagramas";
	public static final String JERARQUIA_TAG = "Jerarquia";
	public static final String VALIDACION_TAG = "Validacion";
	public static final String OBSERVACIONES_TAG = "Observaciones";

	public static final String GENERICA_TAG = "Generica";
	public static final String DERIVADAS_TAG = "Derivadas";
	
	public static final String IDENTIFICADORES_TAG = "Identificadores";
	public static final String IDENTIFICADOR_TAG = "Identificador";
	public static final String COMPONENTES_TAG = "Componentes";
	public static final String COMPONENTE_TAG = "Componente";
	
	public static final String PROYECTO_TAG = "Proyecto";
	public static final String MODELO_TAG = "Modelo";
	public static final String REPRESENTACION_TAG = "Representacion";
	public static final String REPRESENTACIONES_TAG = "Representaciones";
	
	/**********************************************************************************/
	
	public static final String DIAGRAMA_COMPONENTES_QUERY = "./Componentes/Componente";
	public static final String DIAGRAMA_DIAGRAMAS_QUERY = "./Diagramas/Diagrama";
	public static final String DIAGRAMA_QUERY = "./Diagrama";
	
	public static final String ATRIBUTOS_QUERY = "./Atributos/Atributo";

	public static final String IDENTIFICADORES_QUERY = "./Identificadores/Identificador";
	
	public static final String ID_QUERY = "//*[@id='%s']";
	public static final String ID_CHILD_QUERY = ".//*[@id='%s']";
	public static final String FORMULA_QUERY = "./Formula";
	public static final String ORIGINAL_QUERY = "./Origen/RefAtributo";
	public static final String GENERICA_QUERY = "./Generica/RefEntidad";
	public static final String DERIVADAS_QUERY = "./Derivadas/RefEntidad";
	public static final String PARTICIPANTES_QUERY = "./Participantes/Participante";
	public static final String ENTIDAD_REF_QUERY = "./RefEntidad";
	public static final String ATRIBUTO_REF_QUERY = "./RefAtributo";
	public static final String ROL_QUERY = "./Rol";
	public static final String CARDINALIDAD_QUERY = "./Cardinalidad";
	public static final String VALIDACION_QUERY = "./Validacion";
	public static final String OBSERVACIONES_QUERY = "./Observaciones";
	
	public static final String POSICION_QUERY = "./Posicion";
	public static final String DIMENSION_QUERY = "./Dimension";
	/**
	 * Busca el elemento padre que sea Diagrama para el elemento con id especificado.
	 */
	public static final String DIAGRAMA_PADRE_QUERY = "./ancestor::Diagrama";
	public static final String REPRESENTACION_ID_QUERY = "//Representacion[@id='%s']";
	public static final String ELEMENTOS_PRIMER_NIVEL_QUERY = "./*[name()!='Validacion']";
	
	/*******************************************************************************************/
	
	public static final String DIAGRAMA_LOGICO_QUERY = "./DiagramaLogico";
	
	public static final String DIAGRAMA_LOGICO_TAG = "DiagramaLogico";
	public static final String TABLA_TAG = "Tabla";
	public static final String CLAVE_PRIMARIA_TAG = "ClavePrimaria";
	public static final String CLAVE_FORANEA_TAG = "ClaveForanea";
	public static final String TABLAS_TAG = "Tablas";
	public static final String DIAGRAMA_TABLAS_QUERY = "./Tablas/Tabla";
	public static final String TABLA_REFERENCIADA_TAG = "TablaReferenciada";
	public static final String FORANEAS_QUERY = "./ClaveForanea";
	public static final String TABLA_REFERENCIADA_QUERY = "./TablaReferenciada";
	public static final String CLAVE_PRIMARIA_QUERY = "./ClavePrimaria";
	
	public static final String REPRESENTACION_LOGICA_TAG = "RepresentacionLogica";
	public static final String REPRESENTACION_LOGICA_ID_QUERY = "//RepresentacionLogica[@id='%s']";
	public static final String DIAGRAMA_LOGICO_PADRE_QUERY = "./ancestor::DiagramaLogico";
	
	public static final String DER_TAG = "Der";
	
}
