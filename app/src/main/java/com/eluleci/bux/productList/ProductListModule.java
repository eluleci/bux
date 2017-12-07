package com.eluleci.bux.productList;

import com.eluleci.bux.di.ActivityScoped;
import com.eluleci.bux.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Provides product list related dependencies.
 */
@Module
public abstract class ProductListModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ProductListFragment productListFragment();

    @ActivityScoped
    @Binds abstract ProductListContract.Presenter productListPresenter(ProductListPresenter presenter);
}
