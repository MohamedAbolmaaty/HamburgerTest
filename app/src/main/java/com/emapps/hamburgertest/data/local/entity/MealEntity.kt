package com.emapps.hamburgertest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emapps.hamburgertest.domain.model.MealType
import com.emapps.hamburgertest.domain.model.Meal
import com.squareup.moshi.Json

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey val id: Int,
    var type: MealType,
    val name: String,
    val price: Double,
    val description: String,
    val isVegan: Boolean,
    val isHot: Boolean,
    val rating: Float,
    val image: String,
    val isAvailable: Boolean,
) {
    fun toMeal(): Meal = object : Meal {
        override val id = this@MealEntity.id
        override var type = this@MealEntity.type
        override val name = this@MealEntity.name
        override val price = this@MealEntity.price
        override val description = this@MealEntity.description
        override val isVegan = this@MealEntity.isVegan
        override val isHot = this@MealEntity.isHot
        override val rating = this@MealEntity.rating
        override val image = this@MealEntity.image
        override val isAvailable = this@MealEntity.isAvailable
    }
}
