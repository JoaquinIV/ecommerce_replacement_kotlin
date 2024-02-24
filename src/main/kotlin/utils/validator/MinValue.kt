package utils.validator

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Validador que valida tamaño mínimo de un numero en una propiedad de una clase
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class MinValue(val value: Int)