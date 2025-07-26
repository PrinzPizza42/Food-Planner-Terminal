package de.luca

import de.luca.foodPlaner.FoodPlanerManager

fun main() {
    //TODO add ascII art
    Data.loadDishList()
    Data.loadSettings()

    while(true) {
        printCommands()
        println("||MainMenu, enter command>>")
        val input = readln()
        when(input.lowercase()) {
            "dish list" -> DishListManager.mainMenu()
            "planner" -> FoodPlanerManager.mainMenu()
            "exit" -> {
                Data.saveDishList()
                Data.saveSettings()
                println("Exiting...")
                break
            }
        }
    }
}

fun printCommands() {
    println("--- Commands ---")
    println("dish list: enter dish list manager")
    println("planner: enter food planner")
    println("exit: exit the program")
    println("-----------------")
}