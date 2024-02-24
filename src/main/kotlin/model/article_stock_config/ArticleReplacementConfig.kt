package model.article_stock_config

import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonRepresentation
import utils.errors.SimpleError
import utils.errors.ValidationError
import java.util.*


class ArticleReplacementConfig (entityRoot: ArticleReplacementConfigEntity) {

    var entity : ArticleReplacementConfigEntity = entityRoot
        private set

    /**
     * Deshabilitar la configuración
     */
    fun disable(): ArticleReplacementConfig {
        entity = entity.copy(
            enabled = false,
            updated = Date()
        )
        return this
    }

    /**
     * Actualiza la el stock mínimo de la configuración
     */
    fun updateMinStock(data: Int): ArticleReplacementConfig {
        entity = entity.copy(
            minStock = data,
            updated = Date()
        )
        return this
    }

    /**
     * Actualiza la el stock mínimo de la configuración
     */
    fun updateMinQuantityOrder(data: Int): ArticleReplacementConfig {
        if (data < 1) {
            throw SimpleError("Cantidad de pedido mínimo debe ser mayor a 1")
        }
        entity = entity.copy(
            minOrderQuantity = data,
            updated = Date()
        )
        return this
    }

    /**
     * Controla el stock y devuelve la cantidad a reponer
     */
    fun controlarStock(data: Int): Int {
        if(data > entity.minStock) {
            return 0;
        }
        var resposicion = data
        while (resposicion < entity.minStock) {
            resposicion += entity.minOrderQuantity;
        }
        return resposicion;
    }

}


data class ArticleReplacementConfigEntity(
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    val id: String? = null,
    val articleId: String,
    val minStock: Int = 10,
    val minOrderQuantity: Int = 10,
    val updated: Date = Date(),
    val created: Date = Date(),
    val enabled: Boolean = true
)