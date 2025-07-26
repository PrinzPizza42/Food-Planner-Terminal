package de.luca.foodPlaner

import de.luca.Data
import de.luca.DishType

object FoodPlanerSettings {
    var foodPlanerSettingsData = FoodPlanerSettingsData()

    fun mainMenu() {
        printCommands()

        while(true) {
            println("||MainMenu->FoodPlaner->Settings, enter command>>")
            val input = readln()
            when(input) {
                "meals" -> mealsPerDay()
                "weekEndDiffers" -> weekEndDiffers()
                "weekEndMeals" -> mealsPerDayWeekend()
                "exit" -> {
                    println("Exiting to food planer...")
                    break
                }
            }
        }
    }

    fun printCommands() {
        println("--- Commands ---")
        println("meals: change the meals a day should have")
        println("weekEndDiffers: change if the weekend differs from the normal week")
        println("weekEndMeals: change the meals a day on a weekend should have")
//        println("useCalories: change if the calories should be used to plan the meals")
//        println("calories: set the max calories a day can have")
        println("exit: exit to food planer")
        println("-----------------")
    }

    fun mealsPerDay() {
        mealsPerDayHelper(foodPlanerSettingsData.mealsPerDay)
    }

    fun weekEndDiffers() {
        println("Should the weekend differ from the normal week? (Yes / No): ")
        val input = readln()
        foodPlanerSettingsData.weekEndDiffers = input.lowercase() == "yes"
        println("Successfully set weekEndDiffers to: " + foodPlanerSettingsData.weekEndDiffers)
    }

    fun mealsPerDayWeekend() {
        mealsPerDayHelper(foodPlanerSettingsData.mealsPerDayWeekend)
    }

    //TODO
    fun useCalories() {

    }

    //TODO
    fun maxCalories() {

    }

    fun mealsPerDayHelper(list: MutableList<DishType>) {
        val dishTypesString = DishType.entries.joinToString(", ") { it.name }
        println("Available options: " + dishTypesString)
        println("Every lined up meal type stands for one meal, while its type is the type you entered")
        println("Every meal has to be seperated from the next by a `,` ")
        println("Enter the meals a day should normally have:")
        val input = readln()
        val mealsString = input.split(",").toMutableList()
        val mealsEnum = mutableListOf<DishType>()
        mealsString.forEach {
            val trimmed = it.trim()
            try {
                mealsEnum.addLast(DishType.valueOf(trimmed))
            }
            catch(e: IllegalArgumentException) {
                println("Could not find a dish type for " + trimmed)
                return
            }
        }
        list.clear()
        list.addAll(mealsEnum)
        println("Successfully set meals per day to: " + mealsEnum)
    }
}