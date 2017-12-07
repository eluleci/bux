package com.eluleci.bux.productDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.eluleci.bux.R
import com.eluleci.bux.data.Product
import com.eluleci.bux.productDetail.ProductDetailActivity.Companion.getExtraParamIdName
import com.eluleci.bux.util.ActivityUtils
import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import android.view.MenuItem

fun Context.productDetailIntent(product: Product): Intent {
    return Intent(this, ProductDetailActivity::class.java).apply {
        putExtra(getExtraParamIdName(), product.securityId)
    }
}

/**
 * Contains fragment to show the product detail.
 */
class ProductDetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var fragmentProvider: Lazy<ProductDetailFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        if (supportFragmentManager.findFragmentById(R.id.contentFrame) == null) {
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager,
                    fragmentProvider.get(),
                    R.id.contentFrame
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun getExtraParamIdName(): String = "ProductDetailActivity:id"
    }
}
