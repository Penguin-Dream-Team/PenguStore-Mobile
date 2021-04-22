package store.pengu.mobile.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.services.AccountService
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.services.TermiteService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.storage.UserDataService
import store.pengu.mobile.storage.userDataStore
import store.pengu.mobile.views.MainActivity
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getListsService(
        api: PenguStoreApi,
        productsService: ProductsService,
        store: StoreState
    ): ListsService = ListsService(api, productsService, store)

    @Singleton
    @Provides
    fun getProductsService(
        api: PenguStoreApi,
        store: StoreState
    ): ProductsService =
        ProductsService(api, store)

    @Singleton
    @Provides
    fun getAccountService(
        userDataService: UserDataService,
        api: PenguStoreApi,
        store: StoreState
    ): AccountService =
        AccountService(userDataService, api, store)

    @Singleton
    @Provides
    fun getStoreState(): StoreState = StoreState()

    @Singleton
    @Provides
    fun getApi(store: StoreState): PenguStoreApi = PenguStoreApi(store)

    @Singleton
    @Provides
    fun getUserDataService(@ApplicationContext context: Context): UserDataService =
        UserDataService(context.userDataStore)
}