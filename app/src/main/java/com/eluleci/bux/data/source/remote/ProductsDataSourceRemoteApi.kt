package com.eluleci.bux.data.source.remote

import android.util.Log
import com.eluleci.bux.data.PortfolioPerformance
import com.eluleci.bux.data.Product
import com.eluleci.bux.data.source.ProductsDataSource
import com.eluleci.bux.network.BuxNetworkError
import com.eluleci.bux.network.ProductService
import com.eluleci.bux.network.WSService
import com.eluleci.bux.network.ws.ConnectionInfo
import com.eluleci.bux.network.ws.WSPushMessageWrapper
import com.google.common.base.Optional
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton
import io.reactivex.*

/**
 * Implementation of ProductsDataSource. Gets the data from API.
 */
@Singleton
class ProductsDataSourceRemoteApi @Inject constructor() : ProductsDataSource {

    @Inject
    lateinit var wsApi: WSService

    @Inject
    lateinit var retrofit: Retrofit

    // fetches the product list from api with retrofit
    override fun getProducts(): Flowable<List<Product>> {

        return Flowable.create({ emitter ->
            retrofit.create(ProductService::class.java)
                    .getProducts()
                    .subscribe({ response ->
                        if (response.isSuccessful) {
                            emitter.onNext(response.body()!!)
                        } else {
                            val error = Gson().fromJson<BuxNetworkError>(
                                    response.errorBody()?.string(), BuxNetworkError::class.java
                            )
                            emitter.onError(error)
                        }
                    }, { error ->
                        emitter.onError(error)
                    })
        }, BackpressureStrategy.BUFFER)
    }

    // fetches the product from api with retrofit
    override fun getProduct(id: String): Flowable<Optional<Product>> {

        return Flowable.create({ emitter ->
            retrofit.create(ProductService::class.java).getProduct(id).subscribe {
                // wrap the product with Optional
                emitter.onNext(Optional.of(it))
            }
        }, BackpressureStrategy.BUFFER)
    }

    override fun getProductUpdates(id: String): Flowable<PortfolioPerformance> {
        return Flowable.create({ emitter ->
            wsApi.subscribe(id, object : WSService.Subscriber {
                override fun onMessage(portfolioPerformance: PortfolioPerformance) {
                    emitter.onNext(portfolioPerformance)
                }

                override fun onError(throwable: Throwable) {
                    emitter.onError(throwable)
                }
            })

        }, BackpressureStrategy.BUFFER)
    }

    override fun stopProductUpdates(id: String) {
        wsApi.unsubscribe(id)
    }

    override fun saveProduct(product: Product) {
    }

    private inner class WSListener

    companion object {
        val NORMAL_CLOSURE_STATUS = 1000
    }
}
