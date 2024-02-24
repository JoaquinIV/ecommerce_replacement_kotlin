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
import rabbit.NotificacionPurchaseOrderCreated
import rabbit.itemsToPurchaseOrderCreatedItems
import rest.asArticleId
import rest.authHeader

/**
 * @api {get} /v1/replacement/purchase-orders/ Get ordenes  de reposición
 * @apiName Get Ordenes
 * @apiGroup Orden de reposición
 *
 * @apiUse AuthHeader
 *
 *
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 *
 * [
 *      {
 *          "_id": "{id de la orden}"
 *          "created": "{fecha de creación}",
 *          "updated": "{fecha de actualización}",
 *          "state": "{estado de la orden}"
 *          "items": [
 *              {
 *                  "articleId": "{id del artículo}",
 *                  "quantity": "{cantidad a reponer}"
 *              },
 *              ...
 *          ]
 *      },
 *      ...
 * ]
 *
 *
 * @apiUse Errors
 */
class GetOrders(
    private val repository: PurchaseOrderRepository,
    private val tokenService: TokenService,
) {
    fun init(app: Routing) = app.apply {
        get("/v1/replacement/purchase-orders") {
            //this.call.authHeader.validateTokenIsLoggedIn(tokenService)
            val response = repository.findAll().map { it.toPurchaseOrderData }
            this.call.respond(response)
        }
    }
}
