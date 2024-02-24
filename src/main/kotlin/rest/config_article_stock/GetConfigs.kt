package rest.config_article_stock

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.article_stock_config.ArticleStockConfigRepository
import model.article_stock_config.dto.asArticleReplacementConfigData
import model.security.TokenService
import model.security.validateTokenIsAdminUser
import rest.authHeader

/**
 * @api {get} /v1/replacement/articles-config Buscar todas las configuraciones
 * @apiName Obtener Configuraciones
 * @apiGroup Configuración de reposición de artículo
 * @apiDescription Busca todas las configuraciones en el sistema
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * [
 *  {
 *      "_id": "{id de la configuración}"
 *      "articleId": "{id del artículo asociado}",
 *      "minOrderQuantity": "{cantidad mínima que se puede pedir}",
 *      "minStock": "{cantidad mínima de stock}",
 *      "enabled": {si esta activo}
 *  }
 *  ...
 * ]
 *
 * @apiUse Errors
 */
class GetConfigs(
    private val repository: ArticleStockConfigRepository,
    private val tokenService: TokenService,
) {
    fun init(app: Routing) = app.apply {
        get("/v1/replacement/articles-config") {
            //this.call.authHeader.validateTokenIsAdminUser(tokenService)
            val response = repository.findAll().map { it.asArticleReplacementConfigData }
            this.call.respond(response)
        }
    }
}