package de.luca

import de.luca.DishListManager.dishList
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists

object Data {
    val dataPath: Path = Paths.get(System.getProperty("user.home"), ".food_planner")
    val dataPathDishList: Path = dataPath.resolve("dishList.json")

    fun save() {
        println("Saving dish list...")
        val dishListJson = Json.encodeToString(dishList)
        dataPathDishList.toFile().writeText(dishListJson)
    }

    fun load() {
        println("Loading dish list...")
        if(!dataPath.exists()) {
            Files.createDirectories(dataPath)
            println("Created data directory: " + dataPath.toAbsolutePath())
        }
        else {
//            val dishListRead = mapper.readValue(dataPathDishList.toFile(), Array<Dish>::class.java)
//            if(dishListRead != null) dishList.addAll(dishListRead)
            if(dataPathDishList.exists()) {
                dishList.addAll(
                    Json.decodeFromString<List<Dish>>(dataPathDishList.toFile().readText())
                )
                println("Loaded dish list")
            }
            else println("No dish list found")
        }
    }
}