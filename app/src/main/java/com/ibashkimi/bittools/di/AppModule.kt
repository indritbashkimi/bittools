package com.ibashkimi.bittools.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.ibashkimi.shared.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(
            PreferenceHelper.PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    fun providePreferenceHelper(application: Application): PreferenceHelper {
        return PreferenceHelper(application)
    }
}