package de.luca.foodPlaner

import kotlinx.serialization.Serializable

@Serializable
data class Day(
    val meals: MutableList<Meal>,
    val weekDay: WeekDays,
) {
    fun absoluteKCAL(): Int = meals.sumOf { it.dish?.kCal ?: 0 }
    fun mealsWithoutKCAL(): MutableList<Meal> = meals.filter {
        meal ->
        val kcal = meal.dish?.kCal
        kcal == 0
    }.toMutableList()

    fun resetMeals() {
        meals.clear()
    }
}
