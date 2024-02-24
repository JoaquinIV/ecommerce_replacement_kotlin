package rabbit

import com.google.gson.annotations.SerializedName
import utils.rabbit.DirectPublisher
import utils.rabbit.RabbitEvent
import utils.validator.Required
import java.util.Date

class EmitPurchaseOrderCanceled {
    companion object {
        /**
         *
         * @api {direct} replacement/model.purchase-order-canceled Notificación de creación de orden
         *
         * @apiGroup RabbitMQ POST
         *
         * @apiDescription Enviá de mensajes model.purchase-order-canceled desde replacement.
         *
         * @apiSuccessExample {json} Mensaje
         * {
         *      "type": "order-canceled",
         *      "message": {
         *          "orderId": "{orderId}",
         *          "updateDate": "{updateDate}",
         *      }
         * }
         */
        fun sendPurchaseOrderCanceled(exchange: String?, queue: String?, send: NotificacionPurchaseOrderCanceled) {
            val eventToSend = RabbitEvent(
                type = "order-canceled",
                message = send
            )
            DirectPublisher.publish(exchange, queue, eventToSend)
        }
    }
}


data class NotificacionPurchaseOrderCanceled(

    @Required
    @SerializedName("orderId")
    val orderId: String,

//    @Required
//    @SerializedName("updateDate")
//    val updateDate: Date,

) {
    fun publishOn(exchange: String?, queue: String?) {
        EmitPurchaseOrderCanceled.sendPurchaseOrderCanceled(
            exchange, queue, this
        )
    }
}