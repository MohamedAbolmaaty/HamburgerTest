package com.emapps.hamburgertest.domain

import com.emapps.hamburgertest.domain.model.Meal
import com.emapps.hamburgertest.domain.model.MealType
import com.emapps.hamburgertest.domain.repository.MealRepository
import com.emapps.hamburgertest.domain.usecase.GetMealsUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetMealsUseCaseTest {

    private lateinit var useCase: GetMealsUseCase
    private val repository = mockk<MealRepository>()
    private val meal = object : Meal {
        override val id = 101
        override var type = MealType.HAMBURGERS
        override val name = "Classic Beef Burger"
        override val price = 8.99
        override val description = "Grilled beef patty with lettuce, tomato, and cheese"
        override val isVegan = false
        override val isHot = false
        override val rating = 4.2f
        override val image = "https://example.com/images/classic-beef-burger.jpg"
        override val isAvailable = true
    }

    @Before
    fun setUp() {
        useCase = GetMealsUseCase(repository)
    }

    @Test
    fun `invoke returns meals from repository`() = runTest {
        every { repository.getMeals() } returns flowOf(listOf(meal))

        val result = useCase().toList().first()

        assertEquals(listOf(meal), result)
    }

    @Test
    fun `refresh calls repository refreshMeals`() = runTest {
        coEvery { repository.refreshMeals() } returns Unit

        useCase.refresh()

        io.mockk.coVerify { repository.refreshMeals() }

    }
}