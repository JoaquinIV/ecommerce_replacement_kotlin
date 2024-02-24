package rabbit

class Consumers(
    private val consumeAuthLogout: ConsumeAuthLogout,
    private val consumeCatalogArticleData: ConsumeCatalogArticleData,
) {
    fun init() {
        consumeAuthLogout.init()
        consumeCatalogArticleData.init()
    }
}