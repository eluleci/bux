package com.eluleci.bux.data.source.cache

import com.eluleci.bux.data.PortfolioPerformance
import com.eluleci.bux.data.Product
import com.eluleci.bux.data.source.ProductsDataSource
import com.google.common.base.Optional
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ProductsDataSource. Keeps the data in a map, works as a cache mechanism.
 */
@Singleton
class ProductsDataSourceCache @Inject constructor() : ProductsDataSource {

    private val cache = mutableMapOf<String, Product>()

    // converts the cache map values to product list and returns
    override fun getProducts(): Flowable<List<Product>> {
        return Flowable.just(cache.values.toList())
    }

    // searches product from cache map, returns empty otherwise
    override fun getProduct(id: String): Flowable<Optional<Product>> {
        return if (cache.containsKey(id)) {
            Flowable.just(Optional.of(cache[id]!!))
        } else {
            Flowable.empty()
        }
    }

    override fun getProductUpdates(id: String): Flowable<PortfolioPerformance> {
        return Flowable.empty()
    }

    override fun stopProductUpdates(id: String) {
    }

    override fun saveProduct(product: Product) {
        cache.put(product.securityId, product)
    }
}
