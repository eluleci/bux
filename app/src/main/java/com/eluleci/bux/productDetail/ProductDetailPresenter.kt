package com.eluleci.bux.productDetail

import com.eluleci.bux.data.source.ProductsRepository
import com.eluleci.bux.di.ActivityScoped
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Implementation of ProductDetailContract.Presenter. Gets related
 * product info from repository, manipulates it and controls the view state.
 */
@ActivityScoped
class ProductDetailPresenter @Inject constructor(
        val productId: String,
        val repository: ProductsRepository
) : ProductDetailContract.Presenter {

    var view: ProductDetailContract.View? = null

    override fun loadProduct() {
        view?.setLoadingIndicator(true)

        repository.getProduct(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val product = it.get()

                    view?.setLoadingIndicator(false)
                    view?.showProduct(product)

                    getProductUpdates()
                }
    }

    override fun takeView(view: ProductDetailContract.View) {
        this.view = view
        loadProduct()
    }

    override fun dropView() {
        this.view = null
        repository.stopProductUpdates(productId)
    }

    override fun getProductUpdates() {
        repository
                .getProductUpdates(productId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ portfolioPerformance ->
                    view?.showProductUpdate(portfolioPerformance)
                }, { error ->
                    view?.showError(error)
                })
    }
}