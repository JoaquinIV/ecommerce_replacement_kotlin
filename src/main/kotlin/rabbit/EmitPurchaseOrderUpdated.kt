package rabbit

import com.google.gson.annotations.SerializedName
import model.purchase_order.PurchaseOrder
import utils.rabbit.DirectPublisher
import utils.rabbit.RabbitEvent
import utils.validator.Required
import java.util.Date

class EmitPurchaseOrderUpdated {
    companion object {
        /**
         *
         * @api {direct} replacement/model.purchase-order-updated Notificación de actualización de orden
         *
         * @apiGroup RabbitMQ POST
         *
         * @apiDescription Enviá de mensajes model.purchase-order-updated desde replacement.
         *
         * @apiSuccessExample {json} Mensaje
         * {
         *      "type": "order-updated",
         *      "message": {
         *          "orderId": "{orderId}",
         *          "updateDate": "{updateDate}",
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
        fun sendPurchaseOrderUpdated(exchange: String?, queue: String?, send: NotificacionPurchaseOrderUpdated) {
            val eventToSend = RabbitEvent(
                type = "order-updated",
                message = send
            )
            DirectPublisher.publish(exchange, queue, eventToSend)
        }
    }
}


data class NotificacionPurchaseOrderUpdated(

    @Required
    @SerializedName("orderId")
    val orderId: String,

    @Required
    @SerializedName("updateDate")
    val updateDate: Date,

    @SerializedName("purchaseOrderItems")
    val purchaseOrderItems: List<PurchaseOrderUpdatedItem>
) {
    fun publishOn(exchange: String?, queue: String?) {
        EmitPurchaseOrderUpdated.sendPurchaseOrderUpdated(
            exchange, queue, this
        )
    }
}

data class PurchaseOrderUpdatedItem(
    @Required
    @SerializedName("articleId")
    val articleId: String,

    @Required
    @SerializedName("quantity")
    val quantity: Int
)

fun PurchaseOrder.itemsToPurchaseOrderUpdateItems(): List<PurchaseOrderUpdatedItem> {
    var items = this.entity.items
    return items.map {
        PurchaseOrderUpdatedItem(
            articleId = it.articleId,
            quantity = it.quantity,
        )
    }.toList()
}