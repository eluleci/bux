package com.eluleci.bux.data.source

import com.eluleci.bux.data.PortfolioPerformance
import com.eluleci.bux.data.Product
import io.reactivex.Flowable
import com.google.common.base.Optional

/**
 * Definition of the data store used for product manipulations
 */
interface ProductsDataSource {

    fun getProducts(): Flowable<List<Product>>

    fun getProduct(id: String): Flowable<Optional<Product>>

    fun getProductUpdates(id: String): Flowable<PortfolioPerformance>

    fun stopProductUpdates(id: String)

    fun saveProduct(product: Product)
}