package model.purchase_order.dto

import com.google.gson.annotations.SerializedName
import model.purchase_order.PurchaseOrder
import model.purchase_order.PurchaseOrderItemEntity
import rest.asArticleId
import rest.asIdToValue
import utils.validator.MinValue
import utils.validator.Required
import utils.validator.validate
import java.util.Date

data class PurchaseOrderData (
    @SerializedName("id")
    @Required
    val id: String,

    @SerializedName("created")
    val created: Date,

    @SerializedName("updated")
    val updated: Date,

    @SerializedName("state")
    val state: String,

    @SerializedName("items")
    val items: List<PurchaseOrderItemData>,
)


data class PurchaseOrderItemData (
    @SerializedName("articleId")
    @Required
    val articleId: String,

    @SerializedName("quantity")
    val quantity: Int,
)


val PurchaseOrder.toPurchaseOrderData: PurchaseOrderData
    get() {
        this.validate;
        return PurchaseOrderData(
            id = this.entity.id.asIdToValue,
            created = this.entity.created,
            updated = this.entity.updated,
            state = this.entity.state.name,
            items = this.entity.items.map {
                PurchaseOrderItemData(
                    articleId = it.articleId,
                    quantity = it.quantity
                )
            }.toList()
        )
    }