import model.articlesStockConfigModule
import model.purchaseOrderModule
import model.db.databaseModule
import model.securityModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import rabbit.Consumers
import rabbit.rabbitModule
import rest.Routes
import rest.articlesRoutesModule
import rest.purchaseOrderRoutesModule
import rest.routesModule

fun main() {
    Server().start()
}

class Server : KoinComponent {
    private val routes: Routes by inject()
    private val consumers: Consumers by inject()

    fun start() {
        startKoin {
            modules(routesModule, databaseModule, rabbitModule, articlesStockConfigModule, purchaseOrderModule, articlesRoutesModule, purchaseOrderRoutesModule, securityModule)
        }
        consumers.init()
        routes.init()
    }
}