package ch.wenksi.photosimilaritygame.di

import androidx.room.Room
import ch.wenksi.photosimilaritygame.datasource.localdb.AppDatabase
import ch.wenksi.photosimilaritygame.domain.logic.ImageProcessor
import ch.wenksi.photosimilaritygame.datasource.randomphoto.PhotosRemote
import ch.wenksi.photosimilaritygame.datasource.randomphoto.UnsplashPhotosRemote
import ch.wenksi.photosimilaritygame.datasource.sharedpreferences.*
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val datasourceModule = module {
    single { ResourceHandler() }

    single<PhotosRemote> { UnsplashPhotosRemote(get(), get()) }

    single<UserSharedPreferences> { EncryptedUserSharedPreferences(androidContext()) }

    single<SettingsSharedPreferences> { BasicSettingsSharedPreferences(androidContext()) }

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "PhotoSimilarityGameDb",
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().resultDao() }

    single { get<AppDatabase>().userDao() }

    single { ImageProcessor(androidContext()) }
}
