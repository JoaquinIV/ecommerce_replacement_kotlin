package model

import model.purchase_order.PurchaseOrderRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val purchaseOrderModule = module {
    singleOf(::PurchaseOrderRepository)
}