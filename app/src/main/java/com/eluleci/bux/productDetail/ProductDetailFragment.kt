package com.eluleci.bux.productDetail

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.eluleci.bux.R
import com.eluleci.bux.data.PortfolioPerformance
import com.eluleci.bux.data.Product
import com.eluleci.bux.di.ActivityScoped
import com.eluleci.bux.network.BuxNetworkError
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Shows the product detail.
 */
@ActivityScoped
class ProductDetailFragment @Inject constructor() : DaggerFragment(), ProductDetailContract.View {

    @Inject
    lateinit var presenter: ProductDetailContract.Presenter

    lateinit var tvCurrentPrice: TextView
    lateinit var tvClosingPrice: TextView
    lateinit var tvMarketStatus: TextView
    lateinit var tvAccountValue: TextView
    lateinit var tvPerformance: TextView
    lateinit var tvSuggestFunding: TextView
    lateinit var progressBar: ProgressBar
    lateinit var coordinatorLayout: CoordinatorLayout

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        // let presenter drop the view to prevent leaks
        presenter.dropView()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(
                R.layout.fragment_product_detail,
                container,
                false
        )

        progressBar = view.findViewById(R.id.progressBar)
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout)
        tvCurrentPrice = view.findViewById(R.id.tvCurrentPrice)
        tvClosingPrice = view.findViewById(R.id.tvClosingPrice)
        tvMarketStatus = view.findViewById(R.id.tvMarketStatus)
        tvAccountValue = view.findViewById(R.id.tvAccountValue)
        tvPerformance = view.findViewById(R.id.tvPerformance)
        tvSuggestFunding = view.findViewById(R.id.tvSuggestFunding)

        return view
    }

    override fun setLoadingIndicator(active: Boolean) {
        progressBar.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun showProduct(product: Product) {
        tvCurrentPrice.text = product.currentPrice.toString()
        tvClosingPrice.text = product.closingPrice.toString()

        tvMarketStatus.apply {
            when (product.productMarketStatus) {
                "OPEN" -> {
                    text = getString(R.string.pd_market_status_open)
                    setTextColor(ContextCompat.getColor(context, R.color.colorActive))
                }
                "CLOSED" -> {
                    text = getString(R.string.pd_market_status_closed)
                    setTextColor(ContextCompat.getColor(context, R.color.colorPassive))
                }
            }
        }

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = product.displayName
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun showProductUpdate(portfolioPerformance: PortfolioPerformance) {
        tvAccountValue.text = portfolioPerformance.accountValue.toString()
        tvPerformance.text = portfolioPerformance.performance

        tvSuggestFunding.apply {
            when (portfolioPerformance.suggestFunding) {
                true -> {
                    text = getString(R.string.pd_market_suggest_funding_yes)
                    setTextColor(ContextCompat.getColor(context, R.color.colorActive))
                }
                false -> {
                    text = getString(R.string.pd_market_suggest_funding_no)
                    setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                }
            }
        }
    }

    // shows a descriptive error message and also shows a 'retry'
    // action if the error is not related to authentication
    override fun showError(error: Throwable) {

        val isAuthError = (error is BuxNetworkError && error.errorCode.startsWith("AUTH"))
        val message = if (isAuthError) R.string.error_auth_message else R.string.pd_error_live_stream_not_working

        // show user a message with snackbar instead of a dialog because user
        // can see the actual content, only the real time updates are not received
        Snackbar.make(
                coordinatorLayout,
                message,
                Snackbar.LENGTH_INDEFINITE
        ).apply {

            // don't show retry action if the error is related to authentication
            // TODO actually this error should finish the user session but I'm not implementing it for the sake of simplicity
            if (!isAuthError) {
                setAction(R.string.action_retry, { _ -> presenter.getProductUpdates() })
            }
        }.show()
    }

}