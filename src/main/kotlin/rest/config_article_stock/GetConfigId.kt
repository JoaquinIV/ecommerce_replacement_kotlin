package rest.config_article_stock

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.article_stock_config.ArticleStockConfigRepository
import model.article_stock_config.dto.asArticleReplacementConfigData
import model.security.TokenService
import model.security.validateTokenIsAdminUser
import rest.asArticleId
import rest.authHeader
import utils.errors.NotFoundError

/**
 * @api {get} /v1/replacement/articles-config/:id Buscar configuración de reposición de artículo
 * @apiName Buscar Configuración
 * @apiGroup Configuración de reposición de artículo
 * @apiParam {string} id Identicicación de la configuración del artículo
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * {
 *      "_id": "{id de la configuración}"
 *      "articleId": "{id del artículo asociado}",
 *      "minOrderQuantity": "{cantidad mínima que se puede pedir}",
 *      "minStock": "{cantidad mínima de stock}",
 *      "enabled": {si esta activo}
 * }
 *
 * @apiUse Errors
 */
class GetConfigId(
    private val repository: ArticleStockConfigRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        get("/v1/replacement/articles-config/{id}") {
            //this.call.authHeader.validateTokenIsAdminUser(tokenService)
            val id = this.call.parameters["id"].asArticleId
            val data = (repository.findById(id) ?: throw NotFoundError("id"))
                .asArticleReplacementConfigData
            this.call.respond(data)
        }
    }
}
