package de.luca

import kotlinx.serialization.Serializable

@Serializable
data class Dish(var name: String, var price: Float = 0.0f, var dishType: dishType? = null) {
//    var priceAsString: String = "$price €"

//    fun printDish() {
//        println("Name: ${name} | Price: ${priceAsString} | Type: ${dishType}")
//    }
}
