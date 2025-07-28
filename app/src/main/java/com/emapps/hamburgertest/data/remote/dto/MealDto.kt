package com.emapps.hamburgertest.data.remote.dto

import com.emapps.hamburgertest.domain.model.MealType
import com.emapps.hamburgertest.domain.model.Meal
import com.squareup.moshi.Json

data class MealDto(
    override val id: Int,
    override var type: MealType = MealType.UNDEFINED,
    override val name: String,
    override val price: Double,
    override val description: String,
    @param:Json(name = "vegan")
    override val isVegan: Boolean,
    @param:Json(name = "hot")
    override val isHot: Boolean,
    override val rating: Float,
    override val image: String,
    @param:Json(name = "available")
    override val isAvailable: Boolean,
) : Meal
