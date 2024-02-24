package rest.replacement_order

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.article_stock_config.ArticleStockConfigRepository
import model.article_stock_config.dto.asArticleReplacementConfigData
import model.article_stock_config.saveIn
import model.purchase_order.PurchasOrderState
import model.purchase_order.PurchaseOrderRepository
import model.purchase_order.dto.toPurchaseOrderData
import model.purchase_order.saveIn
import model.security.TokenService
import model.security.validateTokenIsAdminUser
import rabbit.NotificacionPurchaseOrderCanceled
import rabbit.NotificacionPurchaseOrderReceived
import rest.asArticleId
import rest.asIdToValue
import rest.authHeader
import rest.config_article_stock.asInt
import utils.errors.ValidationError

/**
 * @api {put} /v1/replacement/purchase-orders/:id/cancel Cancelar orden de resposición
 * @apiName Cancelar orden
 * @apiGroup Orden de reposición
 * @apiParam {string} id Identicicación de la orden
 * @apiUse AuthHeader
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * {
 *      "_id": "{id de la orden}"
 *      "created": "{fecha de creación}",
 *      "updated": "{fecha de actualización}",
 *      "state": "{estado de la orden}"
 *      "items": [
 *          {
 *              "articleId": "{id del artículo}",
 *              "quantity": "{cantidad a reponer}"
 *          },
 *          ...
 *      ]
 * }
 *
 * @apiUse Errors
 */
class PutCancelOrder(
    private val repository: PurchaseOrderRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        put("/v1/replacement/purchase-orders/{id}/cancel") {
            //this.call.authHeader.validateTokenIsAdminUser(tokenService)
            val id = this.call.parameters["id"].asArticleId
            (repository.findById(id) ?: throw ValidationError("id" to "Not found")).let {
                it.cancel()
                it.saveIn(repository).let {
                    NotificacionPurchaseOrderCanceled(
                        orderId = it.entity.id.asIdToValue,
                        //updateDate = it.entity.updated
                    ).publishOn(
                        exchange = "replacement",
                        queue = "purchase-order-canceled"
                    )
                    this.call.respond(it.toPurchaseOrderData)
                }
            }
        }
    }
}
