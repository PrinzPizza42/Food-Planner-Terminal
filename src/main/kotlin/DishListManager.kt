package de.luca

import de.luca.Tools.getString

object DishListManager {
    val dishList = mutableListOf<Dish>()
    var skipOverwrite = false
    var allOverWrite = false

    fun mainMenu() {
        printCommands()
        while(true) {
                println("||MainMenu->DishListManager, enter command>>")
            val input = readln()
            when(input) {
                "add" -> addDish()
                "remove" -> removeDish()
                "edit" -> editDish()
                "import" -> Data.importExternalDishList()
                "list" -> listDishes()
                "exit" -> {
                    println("Exiting to main menu...")
                    break
                }
            }
        }
    }

    fun importDish(dish: Dish): Boolean {
        if(checkIfListContainsDish(dish.name)) {
            if(skipOverwrite) {
                println("Skipping import of ${dish.name}")
                return false
            }
            else if(allOverWrite) {
                println("Overwriting dish...")
                dishList.remove(getIfListContainsDish(dish.name)!!)
                dishList.add(dish)
                println("Successfully imported ${dish.name}")
                return true
            }

            println("Dish ${dish.name} already exists, do you want to overwrite it? (yes/no/skip/all)")
            val input: String = getString("yes/no/skip/all", 100)!!.trim().uppercase()
            when(input) {
                "SKIP" -> {
                    skipOverwrite = true
                    println("Skipping all following overwriting imports")
                    return false
                }
                "ALL" -> {
                    allOverWrite = true
                    println("Always overwrite all following imports")
                    dishList.remove(getIfListContainsDish(dish.name)!!)
                    dishList.add(dish)
                    println("Successfully imported ${dish.name}")
                    return true
                }
                "YES" -> {
                    dishList.remove(getIfListContainsDish(dish.name)!!)
                    dishList.add(dish)
                    println("Successfully imported ${dish.name}")
                    return true
                }
                else -> {
                    println("Aborting import of ${dish.name}")
                    return false
                }
            }
        }
        else {
            dishList.add(dish)
            return true
        }
    }

    fun addDish() {
        val dish = Dish("placeholder")
        println("Enter the name of the dish:")
        dish.name = readln()
        if(checkIfListContainsDish(dish.name)) return println("Dish already exists")

        while (true) {
            println("Enter the Type of the dish (${DishType.entries.joinToString(", ")}:")
            val typeAsString = getString("Only enter one word of the following: ${DishType.entries.joinToString(", ")}", 100)!!.trim().uppercase()
            try {
                dish.dishType = DishType.valueOf(typeAsString)
            }
            catch (e: IllegalArgumentException) {
                println("Could not find a dish type for $typeAsString")
                continue
            }
            break
        }

        dishList.add(dish)
    }

    fun removeDish() {
        println("Enter the name of the dish:")
        val name = readln()
        val dish = getIfListContainsDish(name)
        if(dish != null) {
            dishList.remove(dish)
        }
        else println("Dish does not exist in the list")
    }

    fun editDish() {
        println("Enter the name of the dish you want to edit:")
        val name = readln()
        val dish = getIfListContainsDish(name)
        if(dish != null) {
            println("Enter the new name:")
            val newName = readln()
            dish.name = newName
        }
    }

    fun listDishes() {
        println("--- Dish List ---")
        for (dish in dishList) {
            dish.printDish()
        }
    }

    fun printCommands() {
        println("--- Commands ---")
        println("add: adds a new dish")
        println("remove: removes a dish")
        println("edit: edit a dish")
        println("import: import a dish list from a .csv file from ${Data.dataPathImportDishListFolder}")
        println("list: lists all dishes")
        println("exit: exit to main menu")
        println("-----------------")
    }

    fun checkIfListContainsDish(name: String): Boolean {
        dishList.forEach {
            if(it.name == name) return true
        }
        return false
    }

    fun getIfListContainsDish(name: String): Dish? {
        dishList.forEach {
            if(it.name == name) return it
        }
        return null
    }
}