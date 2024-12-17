package ch.wenksi.photosimilaritygame

import android.app.Application
import ch.wenksi.photosimilaritygame.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant


class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApp)
            modules(
                appModule,
                datasourceModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
            )
        }
    }
}


