package de.luca.foodPlaner

import kotlinx.serialization.Serializable

@Serializable
data class FoodPlanerDay(
    val meals: MutableList<FoodPlanerMeal>,
    val weekDay: WeekDays
    //    val absoluteKJ
)
