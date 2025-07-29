package de.luca.foodPlaner

import kotlinx.serialization.Serializable

@Serializable
data class Day(
    val meals: MutableList<Meal>,
    val weekDay: WeekDays
    //    val absoluteKJ
) {
    fun resetMeals() {
        meals.clear()
    }
}
