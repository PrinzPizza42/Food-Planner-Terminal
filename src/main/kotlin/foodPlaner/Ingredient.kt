package de.luca.foodPlaner

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val amount: Int,
    val unit: String,
    val name: String
)