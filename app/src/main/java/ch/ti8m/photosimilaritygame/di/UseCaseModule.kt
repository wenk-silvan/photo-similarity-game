package ch.wenksi.photosimilaritygame.di

import ch.wenksi.photosimilaritygame.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetRandomPhotoUrlUseCase(get()) }

    factory { GetSimilarityScoreUseCase(get()) }

    factory { CreateResultUseCase(get(), get()) }

    factory { GetResultsUseCase(get()) }

    factory { GetResultUseCase(get()) }

    factory { ClearLeaderboardUseCase(get()) }

    factory { LoginUserUseCase(get(), get()) }

    factory { LoginSessionUseCase(get(), get()) }

    factory { LogoutUserUseCase(get(), get()) }

    factory { RegisterUserUseCase(get(), get()) }

    factory { GetSettingsUseCase(get()) }

    factory { UpdateSettingsUseCase(get()) }

    factory { UpdateUserNameUseCase(get()) }
}
