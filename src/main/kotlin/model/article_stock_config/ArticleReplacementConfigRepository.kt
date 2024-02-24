package model.article_stock_config

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import model.db.MongoStore
import org.bson.types.ObjectId

class ArticleStockConfigRepository(store: MongoStore) {

    private val collection = store.collection<ArticleReplacementConfigEntity>("articles_stock_config")

    suspend fun save(article: ArticleReplacementConfig): ArticleReplacementConfig {
        return if (article.entity.id == null) {
            ArticleReplacementConfig(
                article.entity.copy(
                    id = collection.insertOne(article.entity).insertedId?.toString()
                )
            )
        } else {
            collection.replaceOne(Filters.eq("_id", ObjectId(article.entity.id)), article.entity)
            article
        }
    }

    suspend fun findAll(): List<ArticleReplacementConfig> {
        return collection.find().toList().map {
            ArticleReplacementConfig(it)
        }
    }

    suspend fun findById(id: String): ArticleReplacementConfig? {
        return collection.find(Filters.eq("_id", ObjectId(id))).firstOrNull()?.let {
            ArticleReplacementConfig(it)
        }
    }

    suspend fun findByIdAndEnabled(id: String): ArticleReplacementConfig? {
        return collection.find(
            Filters.and(
                Filters.eq("enabled", true),
                Filters.eq("_id", ObjectId(id)))
        ).firstOrNull()?.let {
            ArticleReplacementConfig(it)
        }
    }

    suspend fun findByArticleId(idArticle: String): ArticleReplacementConfig? {
        return collection.find(Filters.eq("articleId", idArticle)).firstOrNull()?.let {
            ArticleReplacementConfig(it)
        }
    }

    suspend fun findByArticleIdAndEnabled(idArticle: String): ArticleReplacementConfig? {
        return collection.find(
            Filters.and(
                Filters.eq("enabled", true),
                Filters.eq("articleId", idArticle))
            ).firstOrNull()?.let {
            ArticleReplacementConfig(it)
        }
    }
}

suspend fun ArticleReplacementConfig.saveIn(repository: ArticleStockConfigRepository): ArticleReplacementConfig {
    return repository.save(this)
}