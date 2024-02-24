package rest.replacement_order

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.purchase_order.PurchasOrderState
import model.purchase_order.PurchaseOrderRepository
import model.purchase_order.dto.NewPurchaseOrderItem
import model.purchase_order.dto.toPurchaseOrderData
import model.purchase_order.dto.toPurchaseOrderItem
import model.purchase_order.saveIn
import model.security.TokenService
import model.security.validateTokenIsAdminUser
import rabbit.NotificacionPurchaseOrderUpdated
import rabbit.itemsToPurchaseOrderUpdateItems
import rest.asArticleId
import rest.asIdToValue
import rest.authHeader
import utils.errors.ValidationError

/**
 * @api {put} /v1/replacement/purchase-orders/:id/receive Agregar artículo a la orden de resposición
 * @apiName Agregar articulo a la orden
 * @apiGroup Orden de reposición
 * @apiParam {string} id Identicicación de la orden
 * @apiUse AuthHeader
 *
 * @apiExample {json} Body
 * {
 *      "articleId": "{id del articulo}",
 *      "quantity": "{cantidad del articulo}",
 * }
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
class PutOrderItem(
    private val repository: PurchaseOrderRepository,
    private val tokenService: TokenService
) {
    fun init(app: Routing) = app.apply {
        put<NewPurchaseOrderItem>("/v1/replacement/purchase-orders/{id}/item") {
            //this.call.authHeader.validateTokenIsAdminUser(tokenService)
            val id = this.call.parameters["id"].asArticleId
            (repository.findById(id) ?: throw ValidationError("id" to "Not found")).let { po ->
                if (po.entity.state != PurchasOrderState.PENDING)
                    throw ValidationError("Estado de la orden no pendiente" to po.entity.state.name)
                po.addItem(it.toPurchaseOrderItem).saveIn(repository).let {
                    NotificacionPurchaseOrderUpdated(
                        orderId = it.entity.id.asIdToValue,
                        updateDate = it.entity.updated,
                        purchaseOrderItems = it.itemsToPurchaseOrderUpdateItems()
                    ).publishOn(
                        exchange = "replacement",
                        queue = "purchase-order-updated"
                    )
                    this.call.respond(it.toPurchaseOrderData)
                }
            }
        }
    }
}
