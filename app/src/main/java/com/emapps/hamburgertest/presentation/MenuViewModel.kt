package com.emapps.hamburgertest.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emapps.hamburgertest.data.service.ConnectivityService
import com.emapps.hamburgertest.domain.model.SortOption
import com.emapps.hamburgertest.domain.usecase.GetMealsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getMealsUseCase: GetMealsUseCase,
    private val connectivityService: ConnectivityService
) : ViewModel() {

    private val _state = MutableStateFlow(MealState())
    val state: StateFlow<MealState> = _state.asStateFlow()

    init {
        fetchMeals()
        observeNetworkStatus()
    }

    private fun fetchMeals() {
        Log.d("Meals", "fetchMeals")
        viewModelScope.launch {
            launch {
                getMealsUseCase().collect { meals ->
                    val sortedMeals = when {
                        _state.value.sortOptions?.contains(SortOption.PRICE_HIGH) == true ->
                            meals.sortedByDescending { it.price }
                        _state.value.sortOptions?.contains(SortOption.PRICE_LOW) == true ->
                            meals.sortedBy { it.price }
                        _state.value.sortOptions?.contains(SortOption.RATING_HIGH) == true ->
                            meals.sortedByDescending { it.rating }
                        _state.value.sortOptions?.contains(SortOption.RATING_LOW) == true ->
                            meals.sortedBy { it.rating }
                        _state.value.sortOptions?.contains(SortOption.VEGAN) == true ->
                            meals.sortedByDescending { it.isVegan }
                        _state.value.sortOptions?.contains(SortOption.NOT_VEGAN) == true ->
                            meals.sortedBy { it.isVegan }
                        _state.value.sortOptions?.contains(SortOption.HOT) == true ->
                            meals.sortedByDescending { it.isHot }
                        _state.value.sortOptions?.contains(SortOption.NOT_HOT) == true ->
                            meals.sortedBy { it.isHot }
                        _state.value.sortOptions?.contains(SortOption.AVAILABLE) == true ->
                            meals.sortedByDescending { it.isAvailable }
                        _state.value.sortOptions?.contains(SortOption.NOT_AVAILABLE) == true ->
                            meals.sortedBy { it.isAvailable }
                        else -> meals
                    }

                    _state.value = _state.value.copy(meals = sortedMeals)
                }
            }

            launch {
                try {
                    Log.d("Meals", "fetchMeals refresh")
                    getMealsUseCase.refresh()
                } catch (e: Exception) {
                    Log.d("Meals", "fetchMeals: $e")
                }
            }
        }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            connectivityService.isNetworkAvailable.collect { isConnected ->
                _state.value = _state.value.copy(isNetworkAvailable = isConnected)
            }
        }
    }

    fun applySortOptions(options: List<SortOption>) {
        _state.value = _state.value.copy(
            sortOptions = options
        )
        fetchMeals()
    }

    fun retryFetch() {
        fetchMeals()
    }
}