package store.pengu.mobile.di

import dagger.Provides
import store.pengu.mobile.states.StoreState
import javax.inject.Singleton

object AppModule {

    @Singleton
    @Provides
    fun getStore(): StoreState = StoreState()
}