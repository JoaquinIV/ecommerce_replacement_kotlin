package model.article_stock_config.dto

import com.google.gson.annotations.SerializedName
import model.article_stock_config.ArticleReplacementConfig
import model.article_stock_config.ArticleReplacementConfigEntity
import utils.validator.MinValue
import utils.validator.Required
import utils.validator.validate

data class NewArticleReplacementConfigData (
    @SerializedName("articleId")
    @Required
    val articleId: String,

    @SerializedName("minOrderQuantity")
    @MinValue(1)
    val minOrderQuantity: Int = 10,

    @SerializedName("minStock")
    @MinValue(1)
    val minStock: Int = 10,

)

val NewArticleReplacementConfigData.toNewArticleStockConfig: ArticleReplacementConfig
    get() {
        this.validate;
        return ArticleReplacementConfig(
            ArticleReplacementConfigEntity(
                articleId = this.articleId,
                minStock = this.minStock,
                minOrderQuantity = this.minOrderQuantity
            )
        )
    }