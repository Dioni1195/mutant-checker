package com.mutant.checker.config.exception.errorcodes;

/**
 * <h1>ServiceErrorCodes</h1>
 * Description
 *
 * @author Dionisio Arango
 * @version 1.0.0
 * @since 1/07/21
 */
public final class ServiceErrorCodes {

    public static final String ERROR_TAMANO_MINIMO = "El numero minimo de filas debe ser 4";
    public static final Integer ERROR_TAMANO_MINIMO_CODE = 10400;
    public static final String ERROR_MATRIZ_NO_CUADRADA = "La matriz debe ser cuadrada";
    public static final Integer ERROR_MATRIZ_NO_CUADRADA_CODE = 10401;
    public static final String ERROR_NO_MUTANTE = "El ADN es de humano";
    public static final Integer ERROR_NO_MUTANTE_CODE = 10402;
    public static final String ERROR_ADN_ALREADY_CHECKED = "La matriz de adn suministrada ya ha sido validada y el resultado es: %s";
    public static final Integer ERROR_ADN_ALREADY_CHECKED_CODE = 10403;
    public static final String ERROR_SECUENCIA_ADN = "La secuencia contiene caracteres erroneos, deben ser (A,T,C,G) y es: %s";
    public static final Integer ERROR_SECUENCIA_ADN_CODE = 10404;
    public static final String TYPE_E = "E";

    private ServiceErrorCodes() {
    }
}
