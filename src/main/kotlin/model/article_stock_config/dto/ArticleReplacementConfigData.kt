package model.article_stock_config.dto

import com.google.gson.annotations.SerializedName
import model.article_stock_config.ArticleReplacementConfig
import rest.asArticleId
import rest.asIdToValue
import utils.validator.MinValue
import utils.validator.Required

/**
 * Objeto valor para artículos.
 */
data class ArticleReplacementConfigData(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("articleId")
    @Required
    val articleId: String,

    @SerializedName("minOrderQuantity")
    @MinValue(1)
    val minOrderQuantity: Int,

    @SerializedName("minStock")
    @MinValue(1)
    val minStock: Int,

    @SerializedName("enabled")
    val enabled: Boolean = true
)

val ArticleReplacementConfig.asArticleReplacementConfigData
    get() = ArticleReplacementConfigData(
        id = this.entity.id.asIdToValue,
        articleId = this.entity.articleId,
        minStock = this.entity.minStock,
        minOrderQuantity = this.entity.minOrderQuantity,
        enabled = this.entity.enabled
    )