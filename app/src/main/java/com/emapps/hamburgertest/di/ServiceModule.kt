package com.emapps.hamburgertest.di

import android.content.Context
import com.emapps.hamburgertest.data.service.ConnectivityService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideConnectivityService(@ApplicationContext context: Context): ConnectivityService {
        return ConnectivityService(context)
    }
}