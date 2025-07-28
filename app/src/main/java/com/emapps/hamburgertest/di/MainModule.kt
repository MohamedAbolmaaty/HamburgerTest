package com.emapps.hamburgertest.di

import com.emapps.hamburgertest.data.local.dao.MealDao
import com.emapps.hamburgertest.data.remote.api.ApiService
import com.emapps.hamburgertest.data.repository.MealRepositoryImpl
import com.emapps.hamburgertest.domain.repository.MealRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideMealRepository(
        mealDao: MealDao,
        apiService: ApiService
    ): MealRepository {
        return MealRepositoryImpl(mealDao, apiService)
    }
}