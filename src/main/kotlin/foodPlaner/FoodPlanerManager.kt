package de.luca.foodPlaner

object FoodPlanerManager {
    fun mainMenu() {
        printCommands()
        while(true) {
            println("||MainMenu->FoodPlaner, enter command>>")
            val input = readln()
            when(input) {
                "settings" -> FoodPlanerSettings.mainMenu()
                "start" -> {
                    println("Starting food planer...")
                    FoodPlaner.mainMenu()
                }
                "exit" -> {
                    println("Exiting to main menu...")
                    break
                }
            }
        }
    }

    fun printCommands() {
        println("--- Commands ---")
        println("settings: configure the settings")
        println("start: start the planer")
        println("exit: exit to main menu")
        println("-----------------")
    }
}