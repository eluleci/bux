package com.eluleci.bux.productList

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.eluleci.bux.R
import com.eluleci.bux.data.Product
import com.eluleci.bux.di.ActivityScoped
import com.eluleci.bux.network.BuxNetworkError
import com.eluleci.bux.productDetail.productDetailIntent
import dagger.android.support.DaggerFragment
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Shows the list of products.
 */
@ActivityScoped
class ProductListFragment @Inject constructor() : DaggerFragment(), ProductListContract.View {

    @Inject
    lateinit var productListPresenter: ProductListContract.Presenter

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onResume() {
        super.onResume()
        productListPresenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        // let presenter drop the view to prevent leaks
        productListPresenter.dropView()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(
                R.layout.fragment_product_list,
                container,
                false
        )

        productRecyclerView = view.findViewById(R.id.productList)
        progressBar = view.findViewById(R.id.progressBar)

        productRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun setLoadingIndicator(active: Boolean) {
        progressBar.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun showProducts(products: List<Product>) {
        productRecyclerView.adapter = ProductListAdapter(products) {
            productListPresenter.openProductDetails(it)
        }
    }

    override fun showProductDetailUI(product: Product) {
        startActivity(context.productDetailIntent(product))
    }

    override fun showError(error: Throwable) {

        val isAuthError = (error is BuxNetworkError && error.errorCode.startsWith("AUTH"))

        var message = getString(R.string.error_unexpected_message)
        when {
            isAuthError -> message = getString(R.string.error_auth_message)
            error is BuxNetworkError -> message = error.toString()
            error is UnknownHostException -> message = getString(R.string.error_network_message)
        }

        AlertDialog.Builder(context).apply {
            setTitle(getString(R.string.error_title))
            setMessage(message)
        }.create().apply {

            // don't show retry action if the error is related to authentication
            // TODO actually this error should finish the user session but I'm not implementing it for the sake of simplicity
            if (!isAuthError) {
                setButton(
                        AlertDialog.BUTTON_POSITIVE,
                        getString(R.string.action_retry),
                        { _, _ -> productListPresenter.loadProducts() }
                )
            }
            setButton(
                    AlertDialog.BUTTON_NEGATIVE,
                    getString(android.R.string.cancel),
                    { _, _ -> activity.finish() }
            )
        }.show()
    }
}
