package com.emapps.hamburgertest.presentation

import com.emapps.hamburgertest.domain.model.Meal
import com.emapps.hamburgertest.domain.model.SortOption

data class MealState(
    val meals: List<Meal> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val sortOptions: List<SortOption>? = null,
    val isNetworkAvailable: Boolean? = null
)
