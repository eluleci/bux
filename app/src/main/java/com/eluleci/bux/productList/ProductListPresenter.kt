package com.eluleci.bux.productList

import com.eluleci.bux.data.Product
import com.eluleci.bux.data.source.ProductsRepository
import com.eluleci.bux.di.ActivityScoped
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Implementation of ProductListContract.Presenter. Gets related
 * product info from repository, manipulates it and controls the view state.
 */
@ActivityScoped
class ProductListPresenter @Inject constructor(
        val repository: ProductsRepository
) : ProductListContract.Presenter {

    private var productListView: ProductListContract.View? = null

    override fun loadProducts() {
        productListView?.setLoadingIndicator(true)

        repository.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { products ->
                            productListView?.setLoadingIndicator(false)
                            productListView?.showProducts(products)
                        },
                        { error ->
                            productListView?.setLoadingIndicator(false)
                            productListView?.showError(error)
                        }
                )
    }

    override fun openProductDetails(product: Product) {
        productListView?.showProductDetailUI(product)
    }

    override fun takeView(view: ProductListContract.View) {
        productListView = view
        loadProducts()
    }

    override fun dropView() {
        productListView = null
    }

}
