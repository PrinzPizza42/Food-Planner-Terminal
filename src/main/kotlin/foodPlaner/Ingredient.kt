package de.luca.foodPlaner

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val amount: Float,
    val unit: String,
    val name: String
)