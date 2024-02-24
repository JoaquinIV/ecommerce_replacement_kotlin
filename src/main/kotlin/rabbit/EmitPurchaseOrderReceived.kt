package rabbit

import com.google.gson.annotations.SerializedName
import model.purchase_order.PurchaseOrder
import utils.rabbit.DirectPublisher
import utils.rabbit.RabbitEvent
import utils.validator.Required
import java.util.Date

class EmitPurchaseOrderReceived {
    companion object {
        /**
         *
         * @api {direct} replacement/model.purchase-order-received Notificación de recepción de orden
         *
         * @apiGroup RabbitMQ POST
         *
         * @apiDescription Enviá de mensajes model.purchase-order-received desde replacement.
         *
         * @apiSuccessExample {json} Mensaje
         * {
         *      "type": "order-received",
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
        fun sendPurchaseOrderReceived(exchange: String?, queue: String?, send: NotificacionPurchaseOrderReceived) {
            val eventToSend = RabbitEvent(
                type = "order-received",
                message = send
            )
            DirectPublisher.publish(exchange, queue, eventToSend)
        }
    }
}


data class NotificacionPurchaseOrderReceived(

    @Required
    @SerializedName("orderId")
    val orderId: String,

//    @Required
//    @SerializedName("updateDate")
//    val updateDate: String,

    @SerializedName("purchaseOrderItems")
    val purchaseOrderItems: List<PurchaseOrderReceivedItem>

) {
    fun publishOn(exchange: String?, queue: String?) {
        EmitPurchaseOrderReceived.sendPurchaseOrderReceived(
            exchange, queue, this
        )
    }
}


data class PurchaseOrderReceivedItem(
    @Required
    @SerializedName("articleId")
    val articleId: String,

    @Required
    @SerializedName("quantity")
    val quantity: Int
)

fun PurchaseOrder.itemsToPurchaseOrderReceivedItems(): List<PurchaseOrderReceivedItem> {
    var items = this.entity.items
    return items.map {
        PurchaseOrderReceivedItem(
            articleId = it.articleId,
            quantity = it.quantity,
        )
    }.toList()
}
