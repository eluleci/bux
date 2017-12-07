package com.eluleci.bux.productList

import com.eluleci.bux.BasePresenter
import com.eluleci.bux.BaseView
import com.eluleci.bux.data.Product

/**
 * Defines the rules between product list view and presenter.
 */
interface ProductListContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        fun showProducts(products: List<Product>)

        fun showProductDetailUI(product: Product)

    }

    interface Presenter : BasePresenter<View> {

        fun loadProducts()

        fun openProductDetails(product: Product)
    }
}
