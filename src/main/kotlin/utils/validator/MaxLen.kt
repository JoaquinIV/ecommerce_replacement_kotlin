package utils.validator

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Validador que valida tamaño máximo de una cadena de caracteres en una propiedad de una clase
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class MaxLen(val value: Int)