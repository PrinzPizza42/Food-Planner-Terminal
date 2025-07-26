package de.luca.foodPlaner

import de.luca.DishType
import kotlinx.serialization.Serializable

@Serializable
data class FoodPlanerSettingsData(
    var mealsPerDay: MutableList<DishType> = mutableListOf(DishType.BREAKFAST, DishType.LUNCH, DishType.DINNER),
    var weekEndDiffers: Boolean = false,
    var mealsPerDayWeekend: MutableList<DishType> = mutableListOf(),
    var useCalories: Boolean = false,
    var maxCalories: Int = 2000
)
