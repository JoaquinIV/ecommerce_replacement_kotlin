package rabbit

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.article_stock_config.*
import model.purchase_order.*
import rest.asArticleId
import rest.asIdToValue
import utils.env.Log
import utils.gson.jsonToObject
import utils.rabbit.DirectConsumer
import utils.rabbit.RabbitEvent
import utils.validator.Required
import utils.validator.validate

class ConsumeCatalogArticleData(
    private val configRepository: ArticleStockConfigRepository,
    private val orderRepository: PurchaseOrderRepository
) {
    fun init() {
        DirectConsumer("replacement", "article-replacement-control").apply {
            addProcessor("article-data") { e: RabbitEvent? -> processArticleData(e) }
            start()
        }
    }

    /**
     *
     * @api {direct} article-replacement-control/model.article-data Validación de Artículos
     *
     * @apiGroup RabbitMQ GET
     *
     * @apiDescription Escucha de mensajes model.article-data desde article-replacement-control.
     * Valida stock de artículo
     *
     * @apiExample {json} Mensaje
     * {
     *      articleId = "{id del artículo}",
     *      price = "{precio del artículo}",
     *      referenceId = "{referencia del carrito}",
     *      stock = "{stock del artículo}",
     *      valid = "{validación del artículo}"
     * }
     */
    private fun processArticleData(event: RabbitEvent?) {
        event?.asEventArticleData?.let {
            try {
                Log.info("RabbitMQ Consume model.article-data : ${it.articleId}")
                it.validate
                CoroutineScope(Dispatchers.IO).launch {
                    (configRepository.findByArticleIdAndEnabled(it.articleId!!) ?: return@launch).let { a ->

                        if (a.controlarStock(it.stock) > 0) {

                            orderRepository.findWithStatePending().let { po ->

                                if(po != null) {

                                    po.addItem(PurchaseOrderItemEntity(
                                        articleId = it.articleId,
                                        quantity = a.controlarStock(it.stock)
                                      )
                                    ).saveIn(orderRepository).let {

                                        Log.info("Order de reposición actualizada : ${it.entity.id.asIdToValue}")

                                        NotificacionPurchaseOrderUpdated(
                                            orderId = it.entity.id.asIdToValue,
                                            updateDate = it.entity.updated,
                                            purchaseOrderItems = it.itemsToPurchaseOrderUpdateItems()
                                        ).publishOn(
                                            exchange = "replacement",
                                            queue = "purchase-order-updated"
                                        )
                                    }

                                } else {

                                    PurchaseOrder(PurchaseOrderEntity()).let { newPo ->

                                        newPo.addItem(PurchaseOrderItemEntity(
                                            articleId = it.articleId,
                                            quantity = a.controlarStock(it.stock)
                                         )
                                        ).saveIn(orderRepository).let {
                                            Log.info("Order de reposición creada : ${it.entity.id}")
                                            NotificacionPurchaseOrderCreated(
                                                orderId = it.entity.id.asArticleId,
                                                creationDate = it.entity.created,
                                                purchaseOrderItems = it.itemsToPurchaseOrderCreatedItems()
                                            ).publishOn(
                                                exchange = "replacement",
                                                queue = "purchase-order-created"
                                            )
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.error(e)
            }
        }
    }
}


data class EventArticleData(
    @Required
    @SerializedName("articleId")
    val articleId: String? = null,

    @Required
    @SerializedName("referenceId")
    val referenceId: String? = null,

    @SerializedName("valid")
    val valid: Boolean = false,

    @SerializedName("price")
    val price: Double = 0.0,

    @SerializedName("stock")
    val stock: Int = 0
)

val RabbitEvent?.asEventArticleData: EventArticleData?
    get() = this?.message?.toString()?.jsonToObject<EventArticleData>()?.validate
