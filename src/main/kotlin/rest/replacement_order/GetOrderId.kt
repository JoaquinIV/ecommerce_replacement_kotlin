package rest.replacement_order

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.article_stock_config.dto.asArticleReplacementConfigData
import model.purchase_order.PurchaseOrder
import model.purchase_order.PurchaseOrderEntity
import model.purchase_order.PurchaseOrderRepository
import model.purchase_order.dto.toPurchaseOrderData
import model.purchase_order.saveIn
import model.security.TokenService
import model.security.validateTokenIsLoggedIn
import rabbit.NotificacionPurchaseOrderCanceled
import rabbit.NotificacionPurchaseOrderCreated
import rabbit.itemsToPurchaseOrderCreatedItems
import rest.asArticleId
import rest.authHeader
import utils.errors.ValidationError

/**
 * @api {get} /v1/replacement/purchase-orders/:id Get orden  de reposición
 * @apiName Get Orden
 * @apiGroup Orden de reposición
 * @apiParam {string} id Identicicación de la orden
 * @apiUse AuthHeader
 *
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 *
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
 *
 * @apiUse Errors
 */
class GetOrderId(
    private val repository: PurchaseOrderRepository,
    private val tokenService: TokenService,
) {
    fun init(app: Routing) = app.apply {
        get("/v1/replacement/purchase-orders/{id}") {
            //this.call.authHeader.validateTokenIsLoggedIn(tokenService)
            val id = this.call.parameters["id"].asArticleId
            (repository.findById(id) ?: throw ValidationError("id" to "Not found")).let {
                this.call.respond(it.toPurchaseOrderData)
            }
        }
    }
}
