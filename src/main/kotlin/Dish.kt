package de.luca

import de.luca.foodPlaner.Ingredient
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate

@Serializable
data class Dish(
    var name: String,
    var complexity: Int = 0,
    var created: String = LocalDate.now().toString(),
    var taste: String = "50 %", //TODO convert to float while reading
    var dishType: DishType? = null,
    var timeToCreate: Float = 0.0f,
    var lastCooked: String? = null,
    var price: Float = 0.0f,
    var ingredients: MutableList<Ingredient> = mutableListOf(),
    var kCal: Int = 0
) {
    @Transient
    var priceAsString: String = "$price €"
    @Transient
    var lastCookedAsLocalDate: LocalDate? = if(lastCooked != null) LocalDate.parse(lastCooked!!) else null

    fun printDish() {
        println("-Name: ${name} | Price: ${priceAsString} | Type: ${dishType}")
    }

    fun convertIngredientStringToData(stringList: MutableList<String>) {

    }
}
