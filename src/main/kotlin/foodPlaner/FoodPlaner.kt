package de.luca.foodPlaner

import de.luca.Data
import de.luca.Dish
import de.luca.DishListManager
import de.luca.DishType
import de.luca.foodPlaner.FoodPlanerSettings.foodPlanerSettingsData

object FoodPlaner {
    val days = mutableListOf<FoodPlanerDay>()
    fun mainMenu() {
        if(days.isEmpty()) {
            addDay("7")
        }
        printCommands()
        while (true) {
            println("||MainMenu->FoodPlaner->Planer, enter command>>")
            val input = readln()
            val splitInput = input.split(" ")
            val inputFirstWord = splitInput[0]
            val inputSecondWord: String? = if(splitInput.size > 1) splitInput[1] else null
            when (inputFirstWord) {
                "list" -> listDays()
                "run" -> generateMealPlan()
                "add" -> addDay(inputSecondWord)
                "remove" -> removeDay()
                "exit" -> {
                    println("Exiting to food planer main menu...")
                    break
                }
            }
        }
    }

    fun printCommands() {
        println("--- Commands ---")
        println("list: lists all days")
        println("run: fills the days with meals based on the settings and dishes")
        println("add: adds a new day at the end")
        println("remove: removes a new day at the end")
//        println("edit: edit day syntax: edit <week> <day>") //TODO implement
        println("exit: exit to food planer main menu")
        println("-----------------")
    }

    fun listDays() {
        if(days.isEmpty()) return println("No days found")
        println("--- Days ---")
        var numberOfWeeks = 0
        days.forEach {
            if(it.weekDay == WeekDays.MONDAY) {
                numberOfWeeks++
                println("--Week $numberOfWeeks--")
            }
            println("   " + (it.weekDay.ordinal + 1) + ": " + it.weekDay)
        }
    }

    fun addDay(inputSecondWord: String?) {
        val amountOfDaysToAdd: Int = inputSecondWord?.toIntOrNull() ?: 1
        if(amountOfDaysToAdd > 100) return println("Can't add more than 100 days")
        for(i in 1..amountOfDaysToAdd) {
            val weekDay: WeekDays =
                if(days.isEmpty()) WeekDays.MONDAY
                else WeekDays.entries[(days.last().weekDay.ordinal + 1) % 7]
            days.addLast(FoodPlanerDay(mutableListOf(), weekDay))
        }
        println("Added $amountOfDaysToAdd day(s) to the plan")
    }

    fun removeDay() {
        if(days.isEmpty()) return println("No days found")
        val lastDay = days.last()
        days.removeLast()
        println("removing " + lastDay.weekDay + ": " + lastDay.meals.size + " meals")
    }

    fun generateMealPlan() {
        for (day in days) {
            val isWeekend = day.weekDay == WeekDays.SATURDAY || day.weekDay == WeekDays.SUNDAY
                val numberOfMeals = if(isWeekend && foodPlanerSettingsData.weekEndDiffers) foodPlanerSettingsData.mealsPerDayWeekend.size else foodPlanerSettingsData.mealsPerDay.size
                val mealsPerDay = if(isWeekend && foodPlanerSettingsData.weekEndDiffers) foodPlanerSettingsData.mealsPerDayWeekend else foodPlanerSettingsData.mealsPerDay
            for (i in 1..numberOfMeals) {
                val dishType = mealsPerDay[i - 1]
                val dish = getRandomDish(dishType)
                if(dish == null) {
                    println("Could not generate a dish for $dishType")
                    continue
                }
                println("found Dish: ${dish.name}")
                day.meals.add(FoodPlanerMeal(dish, null))
                Data.saveGeneratedPlan(days)
            }
        }
    }

    fun getRandomDish(dishType: DishType): Dish? {
        val filteredDishList = DishListManager.dishList.filter { it.dishType == dishType }
        if(filteredDishList.isEmpty()) {
            println("Could not generate a dish because there are no dishes of type: $dishType")
            return null
        }
        return filteredDishList.randomOrNull()
    }
}