package ch.wenksi.photosimilaritygame.di

import ch.wenksi.photosimilaritygame.domain.logic.ConnectivityObserver
import ch.wenksi.photosimilaritygame.domain.logic.NetworkConnectivityObserver
import ch.wenksi.photosimilaritygame.ktorHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { ktorHttpClient }

    single<ConnectivityObserver> { NetworkConnectivityObserver(androidContext()) }
}
