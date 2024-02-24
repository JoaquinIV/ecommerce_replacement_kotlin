package rest

// apiConfig.kt

/**
 * @apiDefine Errors
 *
 * @apiErrorExample {text} 400 Bad Request con información detallada
 *     400 Bad Request
 *     HTTP/1.1 400 Bad Request
 *     {
 *         "path": "{Nombre de la propiedad}",
 *         "message": "{Motivo del error}"
 *     }
 *
 * @apiErrorExample {text} 400 Bad Request con mensaje simplificado
 *     400 Bad Request
 *     HTTP/1.1 400 Bad Request
 *     {
 *         "error": "{Motivo del error}"
 *     }
 *
 * @apiErrorExample {text} 500 Server Error
 *     500 Server Error
 *     HTTP/1.1 500 Server Error
 *     {
 *         "error": "{Motivo del error}"
 *     }
 */

///**
// * @apiDefine UnauthorizedErrors
// *
// * @apiError {UnauthorizedError} usuario no autorizado.
// */
