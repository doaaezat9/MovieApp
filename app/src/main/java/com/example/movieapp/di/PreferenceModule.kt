package com.example.movieapp.di

import android.app.Application
import android.content.Context
import com.example.movieapp.data.local.sharedpreferences.SharedPrefsHelper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PreferenceModule {

    @Provides
    @Singleton
    fun sharedPrefs(@ApplicationContext context: Context): SharedPrefsHelper {
        return SharedPrefsHelper(context)
    }
}


