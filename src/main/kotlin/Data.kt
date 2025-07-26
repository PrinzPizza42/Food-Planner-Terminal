package de.luca

import de.luca.DishListManager.dishList
import de.luca.foodPlaner.FoodPlaner
import de.luca.foodPlaner.FoodPlanerDay
import de.luca.foodPlaner.FoodPlanerSettings.foodPlanerSettingsData
import de.luca.foodPlaner.FoodPlanerSettingsData
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalTime
import kotlin.io.path.exists

object Data {
    val dataPath: Path = Paths.get(System.getProperty("user.home"), ".food_planner")
    val dataPathDishList: Path = dataPath.resolve("dishList.json")
    val dataPathSettings: Path = dataPath.resolve("settings.json")
    val dataPathPlanesFolder: Path = dataPath.resolve("generated_planes")

    fun saveDishList() {
        println("Saving dish list...")
        val dishListJson = Json.encodeToString(dishList)
        dataPathDishList.toFile().writeText(dishListJson)
    }

    fun loadDishList() {
        println("Loading dish list...")
        if(!dataPath.exists()) {
            Files.createDirectories(dataPath)
            println("Created data directory: " + dataPath.toAbsolutePath())
        }
        else {
            if(dataPathDishList.exists()) {
                dishList.addAll(
                    Json.decodeFromString<List<Dish>>(dataPathDishList.toFile().readText())
                )
                println("Loaded dish list")
            }
            else println("No dish list found")
        }
    }

    fun saveSettings() {
        println("Saving settings...")
        val settingsJson = Json.encodeToString(foodPlanerSettingsData)
        dataPathSettings.toFile().writeText(settingsJson)
    }

    fun loadSettings() {
        println("Loading settings...")
        if(!dataPath.exists()) {
            Files.createDirectories(dataPath)
            println("Created data directory: " + dataPath.toAbsolutePath())
        }
        else {
            if(dataPathSettings.exists()) {
                foodPlanerSettingsData = Json.decodeFromString<FoodPlanerSettingsData>(dataPathSettings.toFile().readText())
                println("Loaded settings")
            }
            else println("No saved settings found")
        }
    }

    fun saveGeneratedPlan(days: MutableList<FoodPlanerDay>) {
        if(!dataPathPlanesFolder.exists()) Files.createDirectories(dataPathPlanesFolder)
        println("Saving generated plan...")
        val dataPathPlan = dataPathPlanesFolder.resolve("plan_${LocalDate.now()}_${LocalTime.now()}.txt")
        val daysString = mutableListOf<String>()
        for(day in days) {
            if(day.meals.isEmpty()) continue
            val mealStringList = mutableListOf<String>()
            for(meal in day.meals) {
                mealStringList.add(meal.dish!!.name)
            }

            val mealsString: String = mealStringList.joinToString("\n")
            var dayString =
                "-${day.weekDay}-" +
                "\n$mealsString"
//            daysString.addLast()
        }
        dataPathPlan.toFile().writeText(Json.encodeToString(days))
    }
}