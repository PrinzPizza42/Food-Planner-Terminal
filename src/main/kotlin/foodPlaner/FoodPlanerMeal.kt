package de.luca.foodPlaner

import de.luca.Dish
import de.luca.DishType

data class FoodPlanerMeal(
    var dish: Dish?,
    var time: Float?
) {
    val dishType: DishType? = dish?.dishType
}
