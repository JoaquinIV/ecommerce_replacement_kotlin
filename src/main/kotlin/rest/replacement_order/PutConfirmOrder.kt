package rest.replacement_order

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.purchase_order.PurchaseOrderRepository
import model.purchase_order.dto.toPurchaseOrderData
import model.purchase_order.saveIn
import model.purchase_order.toIso
import model.security.TokenService
import model.security.validateTokenIsAdminUser
import rabbit.NotificacionPurchaseOrderConfirmed
import rabbit.itemsToPurchaseOrderConfirmedItems
import rabbit.itemsToPurchaseOrderCreatedItems
import rest.asArticleId
import rest.asIdToValue
import rest.authHeader
import utils.errors.ValidationError

/**
 * @api {put} /v1/replacement/purchase-orders/:id/confirm Confirmar orden de resposición
 * @apiName Confirmar orden
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
class PutConfirmOrder(
    private val repository: PurchaseOrderRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        put("/v1/replacement/purchase-orders/{id}/confirm") {
            //this.call.authHeader.validateTokenIsAdminUser(tokenService)
            val id = this.call.parameters["id"].asArticleId
            (repository.findById(id) ?: throw ValidationError("id" to "Not found")).let {
                it.confirm()
                it.saveIn(repository).let {
                    NotificacionPurchaseOrderConfirmed(
                        orderId = it.entity.id.asIdToValue,
                        //updateDate = it.entity.updated.toIso,
                        purchaseOrderItems = it.itemsToPurchaseOrderConfirmedItems()
                    ).publishOn(
                        exchange = "replacement",
                        queue = "purchase-order-confirmed"
                    )
                    this.call.respond(it.toPurchaseOrderData)
                }
            }
        }
    }
}
