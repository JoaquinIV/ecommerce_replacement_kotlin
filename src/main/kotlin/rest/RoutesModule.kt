package rest

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import rest.config_article_stock.*
import rest.replacement_order.*
import utils.http.ErrorHandler

val articlesRoutesModule = module {
    singleOf(::PostConfigs)
    singleOf(::PutConfigStockMin)
    singleOf(::PutConfigMinOrderQuantity)
    singleOf(::GetConfigs)
    singleOf(::GetConfigId)
    singleOf(::DeleteConfigId)
}

val purchaseOrderRoutesModule = module {
    singleOf(::PostOrders)
    singleOf(::PutOrderItem)
    singleOf(::DeleteOrderItem)
    singleOf(::GetOrderId)
    singleOf(::GetOrders)
    singleOf(::PutConfirmOrder)
    singleOf(::PutReceivedOrder)
    singleOf(::PutCancelOrder)
}

val routesModule = module {
    singleOf(::ErrorHandler)
    singleOf(::Routes)
}