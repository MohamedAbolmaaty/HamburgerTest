package com.emapps.hamburgertest.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emapps.hamburgertest.data.service.ConnectivityService
import com.emapps.hamburgertest.domain.model.Meal
import com.emapps.hamburgertest.domain.model.MealType
import com.emapps.hamburgertest.domain.model.SortOption
import com.emapps.hamburgertest.domain.usecase.GetMealsUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MealViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MenuViewModel
    private val getMealsUseCase = mockk<GetMealsUseCase>()
    private val connectivityService = mockk<ConnectivityService>()
    private val testDispatcher = StandardTestDispatcher()

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
        Dispatchers.setMain(testDispatcher)
        every { getMealsUseCase.invoke() } returns flowOf(listOf(meal))
        every { connectivityService.isNetworkAvailable } returns flowOf(true)
        coEvery { getMealsUseCase.refresh() } returns Unit
        viewModel = MenuViewModel(getMealsUseCase, connectivityService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state emits meals from use case`() = runTest {
        val state = viewModel.state.value

        assertEquals(listOf(meal), state.meals)
        assertEquals(null, state.isNetworkAvailable)
    }

    @Test
    fun `applySort sorts meals by price desc`() = runTest {
        val meal2 = object : Meal {
            override val id = 102
            override var type = MealType.HAMBURGERS
            override val name = "Spicy Vegan Burger"
            override val price = 9.49
            override val description = "Plant-based patty with jalapeños and spicy sauce"
            override val isVegan = true
            override val isHot = true
            override val rating = 4.7f
            override val image = "https://example.com/images/spicy-vegan-burger.jpg"
            override val isAvailable = true
        }

        every { getMealsUseCase.invoke() } returns flowOf(listOf(meal, meal2))

        viewModel.applySortOptions(listOf(SortOption.PRICE_HIGH))
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.state.value

        assertEquals(listOf(meal2, meal), state.meals)
        assertEquals(listOf(SortOption.PRICE_HIGH), state.sortOptions)
    }
}