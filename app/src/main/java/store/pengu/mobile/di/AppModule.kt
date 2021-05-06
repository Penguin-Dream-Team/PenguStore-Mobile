package store.pengu.mobile.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.services.*
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.storage.UserDataService
import store.pengu.mobile.storage.userDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun getMapsService(@ApplicationContext context: Context): MapsService =
        MapsService(context)

    @Singleton
    @Provides
    fun getBeaconsService(
        api: PenguStoreApi,
        store: StoreState
    ): BeaconsService =
        BeaconsService(api, store)

    @Singleton
    @Provides
    fun getCartService(
        api: PenguStoreApi,
        listsService: ListsService,
        store: StoreState
    ): CartService =
        CartService(api,listsService, store)

    @Singleton
    @Provides
    fun getListsService(
        api: PenguStoreApi,
        store: StoreState
    ): ListsService = ListsService(api, store)

    @Singleton
    @Provides
    fun getProductsService(
        api: PenguStoreApi,
        store: StoreState
    ): ProductsService =
        ProductsService(api, store)

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