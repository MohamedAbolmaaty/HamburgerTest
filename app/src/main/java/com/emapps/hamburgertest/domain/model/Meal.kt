package com.emapps.hamburgertest.domain.model

import com.emapps.hamburgertest.domain.model.MealType
import com.squareup.moshi.Json

interface Meal {
    val id: Int
    var type: MealType
    val name: String
    val price: Double
    val description: String
    val isVegan: Boolean
    val isHot: Boolean
    val rating: Float
    val image: String
    val isAvailable: Boolean
}