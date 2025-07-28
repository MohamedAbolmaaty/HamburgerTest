package com.emapps.hamburgertest.data.repository

import android.util.Log
import com.emapps.hamburgertest.data.local.dao.MealDao
import com.emapps.hamburgertest.data.local.entity.MealEntity
import com.emapps.hamburgertest.domain.model.MealType
import com.emapps.hamburgertest.data.remote.api.ApiService
import com.emapps.hamburgertest.domain.model.Meal
import com.emapps.hamburgertest.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val mealDao: MealDao,
    private val apiService: ApiService
) : MealRepository {

    override fun getMeals(): Flow<List<Meal>> {
        Log.d("Meals", "getMeals")
        return mealDao.getAllMeals().map { entities ->
            entities.map { it.toMeal() }
        }
    }

    override suspend fun refreshMeals() {
        Log.d("Meals", "refreshMeals")
        try {
            val response = apiService.fetchMenu("\$2a\$10\$gKGk.WhhPDqDbokEJkqkUucnE66bIZSSTGM31bo6SUkYjYbRD2Hf6")
            val remoteMeals = mutableListOf<Meal>()
            response.body()?.record?.menu?.apply {
                hamburgers.forEach { it.type = MealType.HAMBURGERS }
                remoteMeals.addAll(hamburgers)
                pasta.forEach { it.type = MealType.PASTA }
                remoteMeals.addAll(pasta)
                drinks.forEach { it.type = MealType.DRINKS }
                remoteMeals.addAll(drinks)
                sandwiches.forEach { it.type = MealType.SANDWICHES }
                remoteMeals.addAll(sandwiches)
            }
            val entities = remoteMeals.map { meal ->
                MealEntity(
                    id = meal.id,
                    type = meal.type,
                    name = meal.name,
                    price = meal.price,
                    description = meal.description,
                    isVegan = meal.isVegan,
                    isHot = meal.isHot,
                    rating = meal.rating,
                    image = meal.image,
                    isAvailable = meal.isAvailable
                )
            }
            mealDao.clearMeals()
            mealDao.insertMeals(entities)
        } catch (e: Exception) {
            throw e
        }
    }
}