package com.eluleci.bux.productList

import android.os.Bundle

import com.eluleci.bux.R
import com.eluleci.bux.util.ActivityUtils

import javax.inject.Inject

import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity

/**
 * Contains fragment to show product list.
 */
class ProductListActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var fragmentProvider: Lazy<ProductListFragment>

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

}