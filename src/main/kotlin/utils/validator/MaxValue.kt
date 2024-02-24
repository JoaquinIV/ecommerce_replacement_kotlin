package utils.validator

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Validador que valida tamaño máximo de un numero en una propiedad de una clase
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class MaxValue(val value: Int)