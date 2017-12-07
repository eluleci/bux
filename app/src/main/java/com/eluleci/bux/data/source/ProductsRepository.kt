package com.eluleci.bux.data.source

import com.eluleci.bux.data.PortfolioPerformance
import com.eluleci.bux.data.Product
import com.eluleci.bux.data.source.cache.Cache
import com.eluleci.bux.data.source.remote.Remote
import com.google.common.base.Optional
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Main data provider for product model. Contains remote and cache
 * repositories, controls the data flow.
 */
@Singleton
class ProductsRepository @Inject constructor(
        @Remote val remoteApiRepository: ProductsDataSource,
        @Cache val cacheRepository: ProductsDataSource
) : ProductsDataSource {

    override fun getProducts(): Flowable<List<Product>> {

        // get the list from cache repository
        val cachedList = cacheRepository.getProducts()

        // get the list from remote repository and save to cache repository
        val remoteList = remoteApiRepository.getProducts().flatMap({ products ->
            Flowable.fromIterable(products).doOnNext({ product ->
                cacheRepository.saveProduct(product)
            }).toList().toFlowable()
        })

        // first get the list from cache repository
        // then get the list from remote repository if cache repository returns an empty list
        return Flowable.concat(cachedList, remoteList)
                .filter { products -> !products.isEmpty() }
                .firstOrError()
                .toFlowable()
    }

    override fun getProduct(id: String): Flowable<Optional<Product>> {

        // get the product from cache repository
        val cachedProduct = cacheRepository.getProduct(id)

        // get the product from remote repository and save to cache repository
        val remoteProduct = remoteApiRepository.getProduct(id)
                .doOnNext { optionalProduct ->
                    if (optionalProduct.isPresent) {
                        cacheRepository.saveProduct(optionalProduct.get())
                    }
                }

        // first get the product from cache repository
        // then get the product from remote repository if cache repository returns empty product
        return Flowable.concat(cachedProduct, remoteProduct)
                .firstElement()
                .toFlowable()
    }

    override fun getProductUpdates(id: String): Flowable<PortfolioPerformance> {
        // product updates are only retrieved from remote repository
        return remoteApiRepository.getProductUpdates(id)
    }

    override fun stopProductUpdates(id: String) {
        remoteApiRepository.stopProductUpdates(id)
    }

    override fun saveProduct(product: Product) {
    }

}