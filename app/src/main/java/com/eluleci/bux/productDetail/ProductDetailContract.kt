package com.eluleci.bux.productDetail

import com.eluleci.bux.BasePresenter
import com.eluleci.bux.BaseView
import com.eluleci.bux.data.PortfolioPerformance
import com.eluleci.bux.data.Product

/**
 * Defines the rules between product detail view and presenter.
 */
interface ProductDetailContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        fun showProduct(product: Product)

        fun showProductUpdate(portfolioPerformance: PortfolioPerformance)

    }

    interface Presenter : BasePresenter<View> {

        fun loadProduct()

        fun getProductUpdates()

    }
}
