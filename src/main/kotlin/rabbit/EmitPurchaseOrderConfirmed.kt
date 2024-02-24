package rabbit

import com.google.gson.annotations.SerializedName
import model.purchase_order.PurchaseOrder
import utils.rabbit.DirectPublisher
import utils.rabbit.RabbitEvent
import utils.validator.Required
import java.util.Date

class EmitPurchaseOrderConfirmed {
    companion object {
        /**
         *
         * @api {direct} replacement/model.purchase-order-confirmed Notificación de confirmación de orden
         *
         * @apiGroup RabbitMQ POST
         *
         * @apiDescription Enviá de mensajes model.purchase-order-confirmed desde replacement.
         *
         * @apiSuccessExample {json} Mensaje
         * {
         *      "type": "order-confirmed",
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
        fun sendPurchaseOrderConfirmed(exchange: String?, queue: String?, send: NotificacionPurchaseOrderConfirmed) {
            val eventToSend = RabbitEvent(
                type = "order-sent",
                message = send
            )
            DirectPublisher.publish(exchange, queue, eventToSend)
        }
    }
}


data class NotificacionPurchaseOrderConfirmed(

    @Required
    @SerializedName("orderId")
    val orderId: String,

//    @Required
//    @SerializedName("updateDate")
//    val updateDate: String,

    @SerializedName("purchaseOrderItems")
    val purchaseOrderItems: List<PurchaseOrderConfirmedItem>
) {
    fun publishOn(exchange: String?, queue: String?) {
        EmitPurchaseOrderConfirmed.sendPurchaseOrderConfirmed(
            exchange, queue, this
        )
    }
}

data class PurchaseOrderConfirmedItem(
    @Required
    @SerializedName("articleId")
    val articleId: String,

    @Required
    @SerializedName("quantity")
    val quantity: Int
)

fun PurchaseOrder.itemsToPurchaseOrderConfirmedItems(): List<PurchaseOrderConfirmedItem> {
    var items = this.entity.items
    return items.map {
        PurchaseOrderConfirmedItem(
            articleId = it.articleId,
            quantity = it.quantity,
        )
    }.toList()
}