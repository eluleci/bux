package com.eluleci.bux.network

import com.eluleci.bux.data.Product
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {

    @GET("products")
    fun getProducts(): Flowable<Response<List<Product>>>

    @GET("products/{id}")
    fun getProduct(@Path("id") id: String): Flowable<Product>
}
