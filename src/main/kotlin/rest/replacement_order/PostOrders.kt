package rest.replacement_order

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.purchase_order.PurchaseOrder
import model.purchase_order.PurchaseOrderEntity
import model.purchase_order.PurchaseOrderRepository
import model.purchase_order.dto.toPurchaseOrderData
import model.purchase_order.saveIn
import model.security.TokenService
import model.security.validateTokenIsAdminUser
import rabbit.NotificacionPurchaseOrderCreated
import rabbit.itemsToPurchaseOrderCreatedItems
import rest.asArticleId
import rest.asIdToValue
import rest.authHeader

/**
 * @api {post} /v1/replacement/purchase-orders/ Crear orden de reposición
 * @apiName Crear Orden
 * @apiGroup Orden de reposición
 *
 * @apiDescription Busca una orden pendiente, si no existe, la crea
 *
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
 * @apiUse Errors
 */
class PostOrders(
    private val repository: PurchaseOrderRepository,
    private val tokenService: TokenService,
) {
    fun init(app: Routing) = app.apply {
        post("/v1/replacement/purchase-orders") {
            //this.call.authHeader.validateTokenIsAdminUser(tokenService)
            repository.findWithStatePending()?.let {
                this.call.respond(it.toPurchaseOrderData)
                return@post
            }
            val po = PurchaseOrder(PurchaseOrderEntity()).saveIn(repository)
            NotificacionPurchaseOrderCreated(
                orderId = po.entity.id.asIdToValue,
                creationDate = po.entity.created,
                purchaseOrderItems = po.itemsToPurchaseOrderCreatedItems()
            ).publishOn(
                exchange = "replacement",
                queue = "purchase-order-created"
            )
            this.call.respond(po.toPurchaseOrderData)
        }
    }
}
