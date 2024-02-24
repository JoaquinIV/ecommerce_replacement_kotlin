package rest

import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import utils.env.Environment
import utils.http.ErrorHandler
import java.io.File
import io.ktor.server.plugins.cors.routing.CORS
import rest.config_article_stock.*
import rest.replacement_order.*

class Routes(
    private val getConfigs: GetConfigs,
    private val getConfigId: GetConfigId,
    private val postConfigs: PostConfigs,
    private val putConfigStockMin: PutConfigStockMin,
    private val putConfigMinOrderQuantity: PutConfigMinOrderQuantity,
    private val deleteConfigId: DeleteConfigId,
    private val deleteOrderItem: DeleteOrderItem,
    private val getOrderId: GetOrderId,
    private val getOrders: GetOrders,
    private val postOrders: PostOrders,
    private val putOrderItem: PutOrderItem,
    private val putCancelOrder: PutCancelOrder,
    private val putConfirmOrder: PutConfirmOrder,
    private val putReceivedOrder: PutReceivedOrder,
    private val errorHandler: ErrorHandler
) {

    fun init() {
        embeddedServer(
            Netty,
            port = Environment.env.serverPort,
            module = {
                install(CORS){
                    anyHost()
                    allowMethod(HttpMethod.Options)
                    allowMethod(HttpMethod.Put)
                    allowMethod(HttpMethod.Patch)
                    allowMethod(HttpMethod.Delete)
                    allowHeader(HttpHeaders.ContentType)
                    allowHeader(HttpHeaders.Authorization)
                }
                install(ContentNegotiation) {
                    gson()
                }
                install(CallLogging)

                errorHandler.init(this)

                routing {
                    staticFiles("/", File(Environment.env.staticLocation))

                    getConfigs.init(this)
                    getConfigId.init(this)
                    postConfigs.init(this)
                    putConfigStockMin.init(this)
                    putConfigMinOrderQuantity.init(this)
                    deleteConfigId.init(this)

                    getOrderId.init(this)
                    getOrders.init(this)
                    postOrders.init(this)
                    postOrders.init(this)
                    deleteOrderItem.init(this)
                    putOrderItem.init(this)
                    putCancelOrder.init(this)
                    putConfirmOrder.init(this)
                    putReceivedOrder.init(this)
                }
            }
        ).start(wait = true)
    }
}
