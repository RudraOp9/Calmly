package com.calmly.di

import com.calmly.data.local.DataStoreManager
import com.calmly.data.repositories.PreferencesRepository
import com.calmly.data.repositories.PreferencesRepositoryImpl
import com.calmly.data.repositories.SoundRepository
import com.calmly.data.repositories.SoundRepositoryImpl
import com.calmly.domain.usecases.PlaySoundUseCase
import com.calmly.domain.usecases.StopSoundUseCase
import com.calmly.presentation.viewmodel.SoundsViewModel
import com.calmly.service.MediaPlaybackService
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Data Layer
    single { DataStoreManager(androidContext()) }
    single<SoundRepository> { SoundRepositoryImpl() }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }

    // Service Layer
    single { MediaPlaybackService() }

    // Domain Layer
    single { PlaySoundUseCase(get()) }
    single { StopSoundUseCase(get()) }

    // Presentation Layer
    viewModel { SoundsViewModel(get(), get(), get(), get()) }
}