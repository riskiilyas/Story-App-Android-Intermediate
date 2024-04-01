package com.riskee.livestorybyriski

import android.app.Application
import androidx.room.Room
import com.riskee.livestorybyriski.data.ApiConfig
import com.riskee.livestorybyriski.data.AppDb
import com.riskee.livestorybyriski.data.AppRepository
import com.riskee.livestorybyriski.ui.add_story.AddStoryViewModel
import com.riskee.livestorybyriski.ui.detail.DetailViewModel
import com.riskee.livestorybyriski.ui.list_story.ListStoryViewModel
import com.riskee.livestorybyriski.ui.login.LoginViewModel
import com.riskee.livestorybyriski.ui.main.MainViewModel
import com.riskee.livestorybyriski.ui.maps.MapsViewModel
import com.riskee.livestorybyriski.ui.register.RegisterViewModel
import com.riskee.livestorybyriski.utils.SharedPrefManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(getInjectionModule())
        }
    }

    private fun getInjectionModule(): Module {
        return module {
            single { ApiConfig.getApiService(get()) }
            single { SharedPrefManager(get()) }
            single {
                Room.databaseBuilder(
                    applicationContext,
                    AppDb::class.java,
                    "my_story_live_db"
                ).build()
            }
            single { AppRepository(get(), get(), get()) }
            viewModel { RegisterViewModel(get()) }
            viewModel { LoginViewModel(get(), get()) }
            viewModel { ListStoryViewModel(get(), get()) }
            viewModel { AddStoryViewModel(get(), get()) }
            viewModel { MainViewModel(get()) }
            viewModel { DetailViewModel(get(), get()) }
            viewModel { MapsViewModel(get(), get()) }
        }
    }
}