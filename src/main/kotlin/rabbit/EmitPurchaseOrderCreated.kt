package rabbit

import com.google.gson.annotations.SerializedName
import model.purchase_order.PurchaseOrder
import utils.rabbit.DirectPublisher
import utils.rabbit.RabbitEvent
import utils.validator.Required
import java.util.Date

class EmitPurchaseOrderCreated {
    companion object {
        /**
         *
         * @api {direct} replacement/model.purchase-order-created Notificación de creación de orden
         *
         * @apiGroup RabbitMQ POST
         *
         * @apiDescription Enviá de mensajes model.purchase-order-created desde replacement.
         *
         * @apiSuccessExample {json} Mensaje
         * {
         *      "type": "order-created",
         *      "message": {
         *          "orderId": "{orderId}",
         *          "creationDate": "{creationDate}",
         *          "purchaseOrderItems": [
         *              {
         *                  "articleId": "{arcticleId}",
         *                  "quantity": "{quantity}"
         *              },
         *              ...
         *          ]
         *      }
         * }
         */
        fun sendPurchaseOrderCreated(exchange: String?, queue: String?, send: NotificacionPurchaseOrderCreated) {
            val eventToSend = RabbitEvent(
                type = "order-created",
                message = send
            )
            DirectPublisher.publish(exchange, queue, eventToSend)
        }
    }
}


data class NotificacionPurchaseOrderCreated(

    @Required
    @SerializedName("orderId")
    val orderId: String,

    @Required
    @SerializedName("creationDate")
    val creationDate: Date,

    @SerializedName("purchaseOrderItems")
    val purchaseOrderItems: List<PurchaseOrderCreatedItem>
) {
    fun publishOn(exchange: String?, queue: String?) {
        EmitPurchaseOrderCreated.sendPurchaseOrderCreated(
            exchange, queue, this
        )
    }
}

data class PurchaseOrderCreatedItem(
    @Required
    @SerializedName("articleId")
    val articleId: String,

    @Required
    @SerializedName("quantity")
    val quantity: Int
)

fun PurchaseOrder.itemsToPurchaseOrderCreatedItems(): List<PurchaseOrderCreatedItem> {
    var items = this.entity.items
    return items.map {
        PurchaseOrderCreatedItem(
            articleId = it.articleId,
            quantity = it.quantity,
        )
    }.toList()
}