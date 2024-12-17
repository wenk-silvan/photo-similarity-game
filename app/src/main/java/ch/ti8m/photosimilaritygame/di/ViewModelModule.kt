package ch.wenksi.photosimilaritygame.di

import ch.wenksi.photosimilaritygame.NavigationViewModel
import ch.wenksi.photosimilaritygame.ui.screens.leaderboard.LeaderboardViewModel
import ch.wenksi.photosimilaritygame.ui.screens.leaderboard_details.LeaderboardDetailsViewModel
import ch.wenksi.photosimilaritygame.ui.screens.login.LoginViewModel
import ch.wenksi.photosimilaritygame.ui.screens.photo.PhotoViewModel
import ch.wenksi.photosimilaritygame.ui.screens.register.RegisterViewModel
import ch.wenksi.photosimilaritygame.ui.screens.result.ResultViewModel
import ch.wenksi.photosimilaritygame.ui.screens.settings.SettingsViewModel
import ch.wenksi.photosimilaritygame.ui.screens.takephoto.TakePhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { NavigationViewModel() }

    viewModel { PhotoViewModel(get(), get(), get()) }

    viewModel { ResultViewModel(get()) }

    viewModel { LeaderboardDetailsViewModel(get()) }

    viewModel { LeaderboardViewModel(get(), get()) }

    viewModel { LoginViewModel(get(), get()) }

    viewModel { RegisterViewModel(get()) }

    viewModel { SettingsViewModel(get(), get(), get(), get(), get()) }

    viewModel { TakePhotoViewModel() }
}
