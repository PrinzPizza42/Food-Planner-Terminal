package de.luca

import de.luca.DishListManager.dishList
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
    val dataPathImportDishListFolder: Path = dataPath.resolve("dish_lists_import")

    fun handleDirSetup() {
        if(!dataPath.exists()) {
            Files.createDirectories(dataPath)
            println("Created data directory: " + dataPath.toAbsolutePath())
        }
        if(!dataPathPlanesFolder.exists()) {
            Files.createDirectories(dataPathPlanesFolder)
        }
        if(!dataPathImportDishListFolder.exists()) {
            Files.createDirectories(dataPathImportDishListFolder)
        }
    }

    fun saveDishList() {
        println("Saving dish list...")
        val dishListJson = Json.encodeToString(dishList)
        dataPathDishList.toFile().writeText(dishListJson)
    }

    fun loadDishList() {
        println("Loading dish list...")
        if(dataPathDishList.exists()) {
            dishList.addAll(
                Json.decodeFromString<List<Dish>>(dataPathDishList.toFile().readText())
            )
            println("Loaded dish list")
        }
        else println("No dish list found")
    }

    fun saveSettings() {
        println("Saving settings...")
        val settingsJson = Json.encodeToString(foodPlanerSettingsData)
        dataPathSettings.toFile().writeText(settingsJson)
    }

    fun loadSettings() {
        println("Loading settings...")
        if(dataPathSettings.exists()) {
            foodPlanerSettingsData = Json.decodeFromString<FoodPlanerSettingsData>(dataPathSettings.toFile().readText())
            println("Loaded settings")
        }
        else println("No saved settings found")
    }

    fun importExternalDishList() {
        println("Importing external dish list...")
        val dishLists = mutableListOf<String>()
        dataPathImportDishListFolder.toFile().listFiles()?.forEach { file -> dishLists.add(file.name) }
        if(dishLists.isEmpty()) {
            println("No dish lists found in $dataPathImportDishListFolder")
            return
        }

        println("Available dish lists:")
        dishLists.forEach { println("${dishLists.indexOf(it) + 1}: $it") }

        println("Enter the number of the list you want to import:")
        val input = Tools.getInt("Please enter a number from 1 to ${dishLists.size}", 1, dishLists.size)
        val fileName = dishLists[input - 1]
        println("Importing $fileName...")
        val dishListCSV = dataPathImportDishListFolder.resolve(fileName).toFile().readText()
        val categories = dishListCSV.split("\n").toMutableList()[0].split(",").toMutableList()
        val categoryDishTypeIndex: Int = if(categories.contains("DishType")) categories.indexOf("Tags") else 4
        println("Categories: ${categories.joinToString("; ")}")
        val dishes = dishListCSV.split("\n").toMutableList().drop(1).toMutableList()
        var importedDishes = 0

        for(dish in dishes) {
            val dishSplit = dish.split(",").toMutableList()
            val dishName = dishSplit[0].trim()
            val dishTypeString = dishSplit[categoryDishTypeIndex].trim()
            val importedDish = Dish(dishName)
            importedDish.dishType = getMappedDishType(dishTypeString)
            val imported: Boolean = DishListManager.importDish(importedDish)
            if(imported) importedDishes++
        }

        println("Successfully imported ${dishes.size} dishes")
        saveDishList()
        DishListManager.skipOverwrite = false
        DishListManager.allOverWrite = false
    }

    private fun getMappedDishType(dishType: String): DishType =
        when(dishType) {
            "Mahlzeit" -> DishType.DINNER
            "Frühstück" -> DishType.BREAKFAST
            "Dessert" -> DishType.DESSERT
            "Snack" -> DishType.SNACK
            else -> DishType.DINNER
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