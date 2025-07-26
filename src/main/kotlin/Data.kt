package de.luca

import de.luca.DishListManager.dishList
import de.luca.foodPlaner.FoodPlaner
import de.luca.foodPlaner.FoodPlanerDay
import de.luca.foodPlaner.FoodPlanerSettings.foodPlanerSettingsData
import de.luca.foodPlaner.FoodPlanerSettingsData
import de.luca.foodPlaner.WeekDays
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalTime
import kotlin.io.path.exists

object Data {
    private val dataPath: Path = Paths.get(System.getProperty("user.home"), ".food_planner")
    private val dataPathDishList: Path = dataPath.resolve("dishList.json")
    private val dataPathSettings: Path = dataPath.resolve("settings.json")
    private val dataPathPlanesFolder: Path = dataPath.resolve("generated_planes")

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
        val localTime = LocalTime.now().toString().replace(":", "_")
        val dataPathPlan = dataPathPlanesFolder.resolve("plan_${LocalDate.now()}_${localTime}.txt")
        val daysString = mutableListOf<String>()

        //Write days
        var numberOfWeeks = 0
        for(day in days) {
            if(day.weekDay == WeekDays.MONDAY) {
                numberOfWeeks++
                daysString.addLast("--Week $numberOfWeeks--")
            }
            if(day.meals.isEmpty()) continue
            val mealStringList = mutableListOf<String>()
            for(meal in day.meals) {
                mealStringList.add("${meal.dish!!.dishType!!.name}: ${meal.dish!!.name}")
            }
            val mealsString: String = mealStringList.joinToString("\n")
            val dayString =
                "-${day.weekDay}-" +
                "\n$mealsString"
            daysString.addLast(dayString)
        }
        dataPathPlan.toFile().writeText(daysString.joinToString("\n\n"))

        //Write ingredients
//        val ingredients = mutableListOf<String>()
//        for(day in days) {
//            if(day.meals.isEmpty()) continue
//            for(meal in day.meals) {
//                if(meal.dish!!.ingredients.isEmpty()) continue
//                val ingredientsString = meal.dish!!.ingredients.joinToString(", ")
//                ingredients.addLast(ingredientsString)
//            }
//        }
//        val ingredientsString = ingredients.joinToString("\n")
    }
}