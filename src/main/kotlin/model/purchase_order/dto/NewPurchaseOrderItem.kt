package model.purchase_order.dto

import com.google.gson.annotations.SerializedName
import model.purchase_order.PurchaseOrderItemEntity
import utils.validator.MinValue
import utils.validator.Required
import utils.validator.validate

data class NewPurchaseOrderItem (
    @SerializedName("articleId")
    @Required
    val articleId: String,

    @SerializedName("quantity")
    @MinValue(1)
    val quantity: Int = 10,
)

val NewPurchaseOrderItem.toPurchaseOrderItem: PurchaseOrderItemEntity
    get() {
        this.validate;
        return PurchaseOrderItemEntity(
            articleId = this.articleId,
            quantity = this.quantity
        )
    }