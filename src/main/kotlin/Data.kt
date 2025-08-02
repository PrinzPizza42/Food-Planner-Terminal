package de.luca

import de.luca.DishListManager.dishList
import de.luca.foodPlaner.*
import de.luca.foodPlaner.FoodPlanerSettings.foodPlanerSettingsData
import kotlinx.serialization.json.Json
import java.nio.file.*
import java.time.LocalDate
import java.time.LocalTime
import kotlin.io.path.appendText
import kotlin.io.path.exists
import kotlin.io.path.writeText

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
        val categoryDishTypeIndex: Int = if(categories.contains("Tags")) categories.indexOf("Tags") else 4
        val categoryIngredientsIndex: Int = if(categories.contains("Zutaten")) categories.indexOf("Tags") else 7
        val categoryKCALIndex: Int = if(categories.contains("KCAL")) categories.indexOf("KCAL") else 8
        println("Categories: ${categories.joinToString("; ")}")
        val dishes = dishListCSV.split("\n").toMutableList().drop(1).toMutableList()
        var importedDishes = 0

        for(dish in dishes) {
            val dishSplit = dish.split(",").toMutableList()
            val dishName = dishSplit[0].trim()
            val importedDish = Dish(dishName)

            //DishType
            val dishTypeString = dishSplit[categoryDishTypeIndex].trim()

            //Ingredients
            val dishIngredients = mutableListOf<Ingredient>()
            dishSplit[categoryIngredientsIndex].trim().split(";").toMutableList().forEach {
                var splitIngredient = it.split(" ")
                if(splitIngredient[0] == "") splitIngredient = splitIngredient.drop(1)
                if(splitIngredient.isEmpty()) return@forEach

                var amount: Float = 1f
                var unit: String = "x"
                var name: String = "name"

                when (splitIngredient.size) {
                    2 -> {
                        name = splitIngredient[1].trim()
                        try {
                            amount = splitIngredient[0].trim().toFloat()
                        }
                        catch (e: NumberFormatException) {
                            println("Could not get ingredient amount ${splitIngredient[0].trim()} from ingredient $name from dish $dishName")
                            return@forEach
                        }
                    }
                    3 -> {
                        unit = splitIngredient[1].trim()
                        name = splitIngredient[2].trim()
                        try {
                            amount = splitIngredient[0].trim().toFloat()
                        }
                        catch (e: NumberFormatException) {
                            println("Could not get ingredient amount ${splitIngredient[0].trim()} from ingredient $name from dish $dishName")
                            return@forEach
                        }
                    }
                }

                dishIngredients.add(Ingredient(amount, unit, name))
            }

            //KCAL
            println(categories)
            val dishKcal = dishSplit[categoryKCALIndex].trim().toIntOrNull()
            if(dishKcal == null) {
                println("Could not get Kcal amount from dish $dishName")
                continue
            }
            importedDish.kCal = dishKcal

            importedDish.dishType = getMappedDishType(dishTypeString)
            importedDish.ingredients = dishIngredients

            val imported: Boolean = DishListManager.importDish(importedDish)
            if(imported) importedDishes++
        }

        println("Successfully imported $importedDishes dish(es)")
        saveDishList()
        DishListManager.skipOverwrite = false
        DishListManager.allOverWrite = false
    }

    private fun getMappedDishType(dishType: String): DishType =
        when(dishType) {
            "Mahlzeit" -> DishType.DINNER
            "Frühstück" -> DishType.BREAKFAST
            "Dessert" -> DishType.DESSERT
            "Snack" -> DishType.SNACK
            else -> DishType.DINNER
        }

    fun saveGeneratedPlan(days: MutableList<Day>) {
        if(!dataPathPlanesFolder.exists()) Files.createDirectories(dataPathPlanesFolder)
        println("Saving generated plan...")
        val localTime = LocalTime.now().toString().replace(":", "_")
        val dataPathPlan = dataPathPlanesFolder.resolve("plan_${LocalDate.now()}_${localTime}.txt")
        val daysString = mutableListOf<String>()

        //Create file
        dataPathPlan.writeText("Food-Planner-Terminal by Luca\n")

        //Days
        dataPathPlan.appendText("---Plan---\n")
        var numberOfWeeks = 0
        for(day in days) {
            if(day.weekDay == WeekDays.MONDAY) {
                numberOfWeeks++
                daysString.addLast("\n/Week $numberOfWeeks")
            }
            if(day.meals.isEmpty()) continue
            val mealStringList = mutableListOf<String>()
            for(meal in day.meals) {
                mealStringList.add("${meal.dish!!.dishType!!.name}: ${meal.dish!!.name}")
            }
            val mealsString: String = mealStringList.joinToString("\n| - ")
            val dayString = "|\n|${day.weekDay}\n| - $mealsString"
            daysString.addLast(dayString)
        }
        dataPathPlan.appendText(daysString.joinToString("\n"))

        //Ingredients
        dataPathPlan.appendText("\n\n---Ingredients---\n\n")
        val mealsWithoutIngredients = mutableListOf<Dish>()

        val ingredients = mutableListOf<Ingredient>()
        val ingredientsAmountMap = HashMap<String, Float>()

        //Get all ingredients
        for(day in days) {
            if(day.meals.isEmpty()) continue
            for(meal in day.meals) {
                val mealIngredients = meal.dish!!.ingredients
                if(mealIngredients.isEmpty()) {
                    mealsWithoutIngredients.add(meal.dish!!)
                    continue
                }
                ingredients.addAll(mealIngredients)
            }
        }

        //convert to and fill HashMap
        for (ing in ingredients) {
            val string = "${ing.unit}  ${ing.name}"
            val amount = ing.amount
            if(ingredientsAmountMap.contains(string)) {
                val amountBefore: Float? = ingredientsAmountMap.get(string)

                if(amountBefore == null) {
                    println("Could not get amount of $string")
                    continue
                }

                val amountAfter: Float = amountBefore + amount
                ingredientsAmountMap.set(string, amountAfter)
            }
            else ingredientsAmountMap.set(string, amount)
        }

        //get string for every ingredient in map
        val ingredientsAsStrings = mutableListOf<String>()
        ingredientsAmountMap.forEach {
            val string = it.key
            val amount = it.value

            val combined = " - $amount $string"
            ingredientsAsStrings.add(combined)
        }

        //combine all strings into one
        val ingredientsCombinedString = ingredientsAsStrings.joinToString("\n")

        //write to file
        dataPathPlan.appendText(ingredientsCombinedString)

        val mealsWithoutIngredientsNames = mutableListOf<String>()
        mealsWithoutIngredients.forEach {
            if(!mealsWithoutIngredientsNames.contains(it.name)) mealsWithoutIngredientsNames.add(it.name)
        }

        dataPathPlan.appendText("\n\n-Meals without specified ingredients-\n")
        dataPathPlan.appendText(" - ${mealsWithoutIngredientsNames.joinToString("\n - ")}")

        println("Saved new plan ${dataPathPlan.fileName} in ${dataPathPlan.parent.toAbsolutePath()}")
    }
}