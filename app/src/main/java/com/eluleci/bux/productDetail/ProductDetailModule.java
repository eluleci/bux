package com.eluleci.bux.productDetail;

import com.eluleci.bux.di.ActivityScoped;
import com.eluleci.bux.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Provides product detail related dependencies.
 */
@Module
public abstract class ProductDetailModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract ProductDetailFragment productDetailFragment();

    @ActivityScoped
    @Binds
    abstract ProductDetailContract.Presenter productDetailPresenter(ProductDetailPresenter presenter);

    @Provides
    @ActivityScoped
    static String provideProductId(ProductDetailActivity activity) {
        return activity.getIntent().getStringExtra(ProductDetailActivity.Companion.getExtraParamIdName());
    }
}
