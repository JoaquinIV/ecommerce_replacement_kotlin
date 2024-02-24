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
import rabbit.NotificacionPurchaseOrderUpdated
import rabbit.itemsToPurchaseOrderUpdateItems
import rest.asArticleId
import rest.asIdToValue
import utils.errors.ValidationError

/**
 * @api {delete} /v1/replacement/purchase-orders/:id/article/:articleId Eliminar artículo de la orden de resposición
 * @apiName Eliminar artículo de la orden
 * @apiGroup Orden de reposición
 * @apiParam {string} id Identicicación de la orden
 * @apiParam {string} articleId Identicicación del artículo
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
class DeleteOrderItem(
    private val repository: PurchaseOrderRepository
) {
    fun init(app: Routing) = app.apply {
        put<NewPurchaseOrderItem>("/v1/replacement/purchase-orders/{id}/article/{articleId}") {
            //this.call.authHeader.validateTokenIsAdminUser(tokenService)
            val id = this.call.parameters["id"].asArticleId
            (repository.findById(id) ?: throw ValidationError("id" to "Not found")).let { po ->
                po.deleteItem(this.call.parameters["articleId"].toString()).saveIn(repository).let {
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
