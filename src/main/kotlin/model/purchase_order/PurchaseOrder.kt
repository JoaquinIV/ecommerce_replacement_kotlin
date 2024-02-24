package model.purchase_order

import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonRepresentation
import utils.errors.SimpleError
import utils.errors.ValidationError
import java.text.SimpleDateFormat
import java.util.*


class PurchaseOrder (entityRoot: PurchaseOrderEntity) {

    var entity : PurchaseOrderEntity = entityRoot
        private set

    /**
     * Cancelar orden de compra
     */
    fun cancel(): PurchaseOrder {
        if (entity.state != PurchasOrderState.PENDING) {
            throw SimpleError("Estado de la orden no pendiente")
        }
        this.entity = entity.copy(
            state = PurchasOrderState.CANCELED,
            updated = Date()
        )
        return this
    }

    /**
     * Confirmar orden de compra
     */
    fun confirm(): PurchaseOrder {
        if (entity.state != PurchasOrderState.PENDING) {
            throw SimpleError("Estado de la orden no pendiente")
        }
        this.entity = entity.copy(
            state = PurchasOrderState.SENT,
            updated = Date()
        )
        return this
    }

    /**
     * Recibir orden de compra
     */
    fun receive(): PurchaseOrder {
        if (entity.state != PurchasOrderState.SENT) {
            throw SimpleError("Estado de la orden no enviada")
        }
        this.entity = entity.copy(
            state = PurchasOrderState.RECEIVED,
            updated = Date()
        )
        return this
    }

    /**
     * Agrgar item
     */
    fun addItem(item: PurchaseOrderItemEntity): PurchaseOrder {

        if (entity.state != PurchasOrderState.PENDING) {
            throw SimpleError("Estado de la orden no pendiente")
        }

        if (entity.items.firstOrNull { it.articleId == item.articleId } != null) {
            return cambiarCantidad(item);
        }

        this.entity = entity.copy(
            items = entity.items.apply { add(item) },
            updated = Date()
        )

        return this
    }


    /**
     * Eliminar item
     */
    fun deleteItem(articleId: String): PurchaseOrder {

        if (entity.state != PurchasOrderState.PENDING) {
            throw SimpleError("Estado de la orden no pendiente")
        }

        if (entity.items.firstOrNull { it.articleId == articleId } == null) {
            throw SimpleError("Articulo no existente en la orden ${articleId}")
        }

        this.entity = entity.copy(
            items = entity.items.apply {
               this.filter {
                   it.articleId != articleId
               }.toMutableList()
            },
            updated = Date()
        )

        return this
    }

    /**
     * Modificar cantidad de item
     */
    fun cambiarCantidad(item: PurchaseOrderItemEntity): PurchaseOrder {

        if (entity.state != PurchasOrderState.PENDING) {
            throw SimpleError("Estado de la orden no enviada")
        }

        if (item.quantity < 1) {
            throw SimpleError("Cantidad de quantity debe ser mayor o igual a 1")
        }

        if (entity.items.firstOrNull { it.articleId == item.articleId } == null) {
            throw SimpleError("Articulo no existente en la orden ${item.articleId}")
        }

        this.entity = entity.copy(
            updated = Date(),
            items = entity.items.apply {
                this.filter {
                    it.articleId != item.articleId
                }.toMutableList().add(item)
            },
        )

        return this
    }

}


data class PurchaseOrderEntity(
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    val id: String? = null,
    val state: PurchasOrderState = PurchasOrderState.PENDING,
    var items: MutableList<PurchaseOrderItemEntity> = mutableListOf<PurchaseOrderItemEntity>(),
    val updated: Date = Date(),
    val created: Date = Date(),
)


val Date.toIso:String
    get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(this)

enum class PurchasOrderState {
    PENDING,
    CANCELED,
    SENT,
    RECEIVED,
}

data class PurchaseOrderItemEntity (
    var articleId: String,
    var quantity: Int,
)