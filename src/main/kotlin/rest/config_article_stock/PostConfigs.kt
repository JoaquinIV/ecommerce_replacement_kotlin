package rest.config_article_stock

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.article_stock_config.ArticleStockConfigRepository
import model.article_stock_config.dto.NewArticleReplacementConfigData
import model.article_stock_config.dto.asArticleReplacementConfigData
import model.article_stock_config.dto.toNewArticleStockConfig
import model.article_stock_config.saveIn
import model.security.TokenService
import model.security.validateTokenIsAdminUser
import rest.authHeader
import utils.errors.SimpleError
import utils.errors.ValidationError

/**
 * @api {post} /v1/replacement/articles-config/ Crear configuración de reposición de artículo
 * @apiName Crear Configuración
 * @apiGroup Configuración de reposición de artículo
 *
 * @apiUse AuthHeader
 *
 * @apiExample {json} Body
 *  {
 *      "articleId": "{id del artículo}",
 *      "minOrderQuantity": "{cantidad mínima que se puede pedir}",
 *      "minStock": "{cantidad mínima de stock}",
 *  }
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 *
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
class PostConfigs(
    private val repository: ArticleStockConfigRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        post<NewArticleReplacementConfigData>("/v1/replacement/articles-config") {
            //this.call.authHeader.validateTokenIsAdminUser(tokenService)
            repository.findByArticleIdAndEnabled(it.articleId).let {
                if (it != null) throw SimpleError("Ya existe una configuración para el artículo ${it.entity.articleId}")
            }
            val result = it.toNewArticleStockConfig.saveIn(repository).asArticleReplacementConfigData
            this.call.respond(result)
        }
    }
}
