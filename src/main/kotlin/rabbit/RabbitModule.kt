package rabbit

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val rabbitModule = module {
    singleOf(::ConsumeCatalogArticleData)
    singleOf(::ConsumeAuthLogout)
    singleOf(::Consumers)
    singleOf(::EmitPurchaseOrderCreated)
    singleOf(::EmitPurchaseOrderUpdated)
    singleOf(::EmitPurchaseOrderConfirmed)
    singleOf(::EmitPurchaseOrderReceived)
    singleOf(::EmitPurchaseOrderCanceled)
}