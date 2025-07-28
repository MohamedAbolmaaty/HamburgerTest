package com.emapps.hamburgertest.domain.repository

import com.emapps.hamburgertest.domain.model.Meal
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    fun getMeals(): Flow<List<Meal>>
    suspend fun refreshMeals()
}