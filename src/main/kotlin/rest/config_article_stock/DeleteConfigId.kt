package rest.config_article_stock

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.article_stock_config.ArticleStockConfigRepository
import model.article_stock_config.saveIn
import model.security.TokenService
import model.security.validateTokenIsAdminUser
import rest.asArticleId
import rest.authHeader
import utils.errors.NotFoundError

/**
 * @api {delete} /replacement/article-config/:id Eliminar Configuración de artículo
 * @apiName Eliminar Configuración
 * @apiGroup Configuración de reposición de artículo
 * @apiParam {string} id Identicicación de la configuración del artículo
 * @apiUse AuthHeader
 *
 * @apiSuccessExample {json} 200 Respuesta
 * HTTP/1.1 200 OK
 *
 * @apiUse Errors
 */
class DeleteConfigId(
    private val repository: ArticleStockConfigRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        delete("/v1/replacement/article-config/{id}") {
            //this.call.authHeader.validateTokenIsAdminUser(tokenService)
            val id = this.call.parameters["id"].asArticleId
            (repository.findById(id) ?: throw NotFoundError("id"))
                .disable()
                .saveIn(repository)
            this.call.respond(HttpStatusCode.OK)
        }
    }
}