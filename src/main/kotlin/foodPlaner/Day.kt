package de.luca.foodPlaner

import kotlinx.serialization.Serializable

@Serializable
data class Day(
    val meals: MutableList<Meal>,
    val weekDay: WeekDays,
) {
    val absoluteKCAL: Int = meals.sumOf { it.dish?.kCal ?: 0 }
    fun resetMeals() {
        meals.clear()
    }
}
