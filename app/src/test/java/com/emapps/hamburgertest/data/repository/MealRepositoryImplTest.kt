package com.emapps.hamburgertest.data.repository

import com.emapps.hamburgertest.data.local.dao.MealDao
import com.emapps.hamburgertest.data.local.entity.MealEntity
import com.emapps.hamburgertest.data.remote.api.ApiService
import com.emapps.hamburgertest.data.remote.dto.MealDto
import com.emapps.hamburgertest.domain.model.MealType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.lang.RuntimeException

class MealRepositoryImplTest {
    private lateinit var repository: MealRepositoryImpl
    private val mealDao = mockk<MealDao>()
    private val apiService = mockk<ApiService>()

    private val mealDto = MealDto(
        id = 101,
        type = MealType.HAMBURGERS,
        name = "Classic Beef Burger",
        price = 8.99,
        description = "Grilled beef patty with lettuce, tomato, and cheese",
        isVegan = false,
        isHot = false,
        rating = 4.2f,
        image = "https://example.com/images/classic-beef-burger.jpg",
        isAvailable = true
    )

    private val mealEntity = MealEntity(
        id = 101,
        type = MealType.HAMBURGERS,
        name = "Classic Beef Burger",
        price = 8.99,
        description = "Grilled beef patty with lettuce, tomato, and cheese",
        isVegan = false,
        isHot = false,
        rating = 4.2f,
        image = "https://example.com/images/classic-beef-burger.jpg",
        isAvailable = true
    )

    @Before
    fun setup() {
        repository = MealRepositoryImpl(mealDao, apiService)
    }

    @Test
    fun `getMeals returns meals from Room`() = runTest {
        every { mealDao.getAllMeals() } returns flowOf(listOf(mealEntity))
        val expectedMeal = mealEntity.toMeal()

        val result = repository.getMeals().toList().first()

        assertEquals(listOf(expectedMeal), result)
    }

    @Test
    fun `refreshMeals fetches from API and updates Room`() = runTest {
        coEvery { apiService.fetchMenu("\$2a\$10\$gKGk.WhhPDqDbokEJkqkUucnE66bIZSSTGM31bo6SUkYjYbRD2Hf6").body()?.record?.menu?.hamburgers } returns listOf(mealDto)
        coEvery { mealDao.clearMeals() } returns Unit
        coEvery { mealDao.insertMeals(listOf(mealEntity)) } returns Unit
        every { mealDao.getAllMeals() } returns flowOf(listOf(mealEntity))

        repository.refreshMeals()
        val result = repository.getMeals().toList().first()

        assertEquals(listOf(mealEntity.toMeal()), result)
    }

    fun `refreshMeals handles API failure`() = runTest {
        coEvery { apiService.fetchMenu("\$2a\$10\$gKGk.WhhPDqDbokEJkqkUucnE66bIZSSTGM31bo6SUkYjYbRD2Hf6") } throws RuntimeException("API error")
        every { mealDao.getAllMeals() } returns flowOf(listOf(mealEntity))

        try {
            repository.refreshMeals()
        } catch (e: Exception) {
            assertEquals("API error", e.message)
        }

        val result = repository.getMeals().toList().first()
        assertEquals(listOf(mealEntity.toMeal()), result)
    }
}