package de.luca.foodPlaner

object FoodPlanerSettings {
    fun mainMenu() {
        printCommands()
        while(true) {
            println("||MainMenu->FoodPlaner->Settings, enter command>>")
            val input = readln()
            when(input) {
                "exit" -> {
                    println("Exiting to food planer...")
                    break
                }
            }
        }
    }

    fun printCommands() {
        println("--- Commands ---")
        println("exit: exit to food planer")
        println("-----------------")
    }
}