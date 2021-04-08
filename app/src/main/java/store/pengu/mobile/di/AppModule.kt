package store.pengu.mobile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.LoginService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getLoginService(api: PenguStoreApi, store: StoreState): LoginService = LoginService(api, store)

    @Singleton
    @Provides
    fun getListsService(api: PenguStoreApi, productsService: ProductsService, store: StoreState): ListsService = ListsService(api, productsService, store)

    @Singleton
    @Provides
    fun getProductsService(api: PenguStoreApi, store: StoreState): ProductsService = ProductsService(api, store)

    @Singleton
    @Provides
    fun getStoreState(): StoreState = StoreState()

    @Singleton
    @Provides
    fun getApi(store: StoreState): PenguStoreApi = PenguStoreApi(store)

    @Singleton
    @Provides
    fun getStore(): String = "skkr"
}