package rest.config_article_stock

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.article_stock_config.ArticleStockConfigRepository
import model.article_stock_config.dto.asArticleReplacementConfigData
import model.article_stock_config.saveIn
import model.security.TokenService
import rest.asArticleId
import utils.errors.ValidationError

/**
 * @api {post} /v1/replacement/article-config/:id/stock-min/:value Actualizar Stock Mínimo
 * @apiName Actualizar Stock Mínimo de Configuración
 * @apiGroup Configuración de reposición de artículo
 * @apiParam {string} id Identicicación de la configuración del artículo
 * @apiParam {int} value Nuevo valor de la cantidad mínica de stock del artículo
 * @apiUse AuthHeader
 *
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
class PutConfigStockMin(
    private val repository: ArticleStockConfigRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        put("/v1/replacement/article-config/{id}/stock-min/{value}") {
            //this.call.authHeader.validateTokenIsAdminUser(tokenService)
            val id = this.call.parameters["id"].asArticleId
            val data = (repository.findByArticleIdAndEnabled(id) ?: throw ValidationError("id" to "Not found")).let {
                it.updateMinStock(this.call.parameters["value"].asInt)
                it.saveIn(repository)
                this.call.respond(it.asArticleReplacementConfigData)
            }
        }
    }
}


val String?.asInt: Int
    get() {
        if (this.isNullOrBlank()) {
            throw ValidationError("Value" to "can not convert to int")
        }
        return this.toInt();
    }