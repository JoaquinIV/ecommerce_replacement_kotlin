package utils.validator

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Validador que una propiedad es distinta de null
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Required(val value: Boolean = true)