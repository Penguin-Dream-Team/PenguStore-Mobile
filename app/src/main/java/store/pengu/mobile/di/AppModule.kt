package store.pengu.mobile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import store.pengu.mobile.states.StoreState
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getStoreState(): StoreState = StoreState()

    @Singleton
    @Provides
    fun getStore(): String = "skkr"
}