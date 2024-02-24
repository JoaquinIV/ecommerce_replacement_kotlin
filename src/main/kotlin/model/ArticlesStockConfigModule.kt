package model

import model.article_stock_config.ArticleStockConfigRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val articlesStockConfigModule = module {
    singleOf(::ArticleStockConfigRepository)
}