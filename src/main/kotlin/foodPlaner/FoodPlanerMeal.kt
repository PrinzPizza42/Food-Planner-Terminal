package de.luca.foodPlaner

import de.luca.Dish
import kotlinx.serialization.Serializable

@Serializable
data class FoodPlanerMeal(
    var dish: Dish?,
    var time: Float?
)
