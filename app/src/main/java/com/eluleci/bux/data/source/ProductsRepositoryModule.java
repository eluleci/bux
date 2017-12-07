package com.eluleci.bux.data.source;

import com.eluleci.bux.data.source.cache.Cache;
import com.eluleci.bux.data.source.cache.ProductsDataSourceCache;
import com.eluleci.bux.data.source.remote.ProductsDataSourceRemoteApi;
import com.eluleci.bux.data.source.remote.Remote;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * Provides Remote and Cache data sources.
 */
@Module
abstract public class ProductsRepositoryModule {

    @Singleton
    @Binds
    @Cache
    abstract ProductsDataSource provideProductsDataSourceCache(ProductsDataSourceCache productsDataSourceCache);

    @Singleton
    @Binds
    @Remote
    abstract ProductsDataSource provideProductsDataSourceRemoteApi(ProductsDataSourceRemoteApi productsDataSourceRemoteApi);
}
