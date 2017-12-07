package com.eluleci.bux.di;

import com.eluleci.bux.productDetail.ProductDetailActivity;
import com.eluleci.bux.productDetail.ProductDetailModule;
import com.eluleci.bux.productList.ProductListActivity;
import com.eluleci.bux.productList.ProductListModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Binds activities to related modules.
 */
@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = ProductListModule.class)
    abstract ProductListActivity productListActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ProductDetailModule.class)
    abstract ProductDetailActivity productDetailActivity();
}
