package com.emapps.hamburgertest.data.remote.responses

import com.emapps.hamburgertest.data.remote.dto.MealDto


class RestaurantMenuResponse (val record: Record)

class Record (val menu: Menu)

class Menu(
    val hamburgers: List<MealDto>,
    val pasta: List<MealDto>,
    val drinks: List<MealDto>,
    val sandwiches: List<MealDto>,
)