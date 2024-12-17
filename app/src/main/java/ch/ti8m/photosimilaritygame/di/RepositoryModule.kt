package ch.wenksi.photosimilaritygame.di

import ch.wenksi.photosimilaritygame.domain.repository.LeaderboardRepository
import ch.wenksi.photosimilaritygame.domain.repository.PhotoRepository
import ch.wenksi.photosimilaritygame.domain.repository.SettingsRepository
import ch.wenksi.photosimilaritygame.domain.repository.UserRepository
import ch.wenksi.photosimilaritygame.repository.BasicLeaderboardRepository
import ch.wenksi.photosimilaritygame.repository.BasicPhotoRepository
import ch.wenksi.photosimilaritygame.repository.BasicSettingsRepository
import ch.wenksi.photosimilaritygame.repository.BasicUserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PhotoRepository> { BasicPhotoRepository(get()) }

    single<LeaderboardRepository> { BasicLeaderboardRepository(get(), get()) }

    single<UserRepository> { BasicUserRepository(get(), get()) }

    single<SettingsRepository> { BasicSettingsRepository(get(), get()) }
}
