package com.emapps.hamburgertest.domain.usecase

import com.emapps.hamburgertest.domain.model.Meal
import com.emapps.hamburgertest.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMealsUseCase @Inject constructor(
    private val repository: MealRepository
) {
    operator fun invoke(): Flow<List<Meal>> = repository.getMeals()
    suspend fun refresh() = repository.refreshMeals()
}