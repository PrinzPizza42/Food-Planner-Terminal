package de.luca

object DishListManager {
    val dishList = mutableListOf<Dish>()

    fun mainMenu() {
        printCommands()
        while(true) {
                println("||MainMenu->DishListManager, enter command>>")
            val input = readln()
            when(input) {
                "add" -> addDish()
                "remove" -> removeDish()
                "list" -> listDishes()
                "exit" -> {
                    println("Exiting to main menu...")
                    break
                }
            }
        }
    }


    fun addDish() {
        println("Enter the name of the dish:")
        val name = readln()
        if(checkIfListContainsDish(name)) return println("Dish already exists")
        else dishList.add(Dish(name))
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