package model.purchase_order

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import model.db.MongoStore
import org.bson.types.ObjectId

class PurchaseOrderRepository(store: MongoStore) {

    private val collection = store.collection<PurchaseOrderEntity>("purchase_order")

    suspend fun save(order: PurchaseOrder): PurchaseOrder {
        return if (order.entity.id == null) {
            PurchaseOrder(
                order.entity.copy(id = collection.insertOne(order.entity)
                    .insertedId?.toString())
            )
        } else {
            collection.replaceOne(Filters.eq("_id", ObjectId(order.entity.id)), order.entity)
            order
        }
    }

    suspend fun findAll(): List<PurchaseOrder> {
        return collection.find().toList().map {
            PurchaseOrder(it)
        }
    }

    suspend fun findById(id: String): PurchaseOrder? {
        return collection.find(Filters.eq("_id", ObjectId(id))).firstOrNull()?.let {
            PurchaseOrder(it)
        }
    }

    suspend fun findWithStatePending(): PurchaseOrder? {
        return collection.find(Filters.eq("state", PurchasOrderState.PENDING)).firstOrNull()?.let {
            PurchaseOrder(it)
        }
    }
//
//    suspend fun findByIdAndEnabled(id: String): ArticleReplacementConfig? {
//        return collection.find(
//            Filters.and(
//                Filters.eq("enabled", true),
//                Filters.eq("_id", ObjectId(id)))
//        ).firstOrNull()?.let {
//            ArticleReplacementConfig(it)
//        }
//    }
//
//    suspend fun findByArticleId(idArticle: String): ArticleReplacementConfig? {
//        return collection.find(Filters.eq("articleId", idArticle)).firstOrNull()?.let {
//            ArticleReplacementConfig(it)
//        }
//    }
//
//    suspend fun findByArticleIdAndEnabled(idArticle: String): ArticleReplacementConfig? {
//        return collection.find(
//            Filters.and(
//                Filters.eq("enabled", true),
//                Filters.eq("articleId", idArticle))
//            ).firstOrNull()?.let {
//            ArticleReplacementConfig(it)
//        }
//    }
}

suspend fun PurchaseOrder.saveIn(repository: PurchaseOrderRepository): PurchaseOrder {
    return repository.save(this)
}