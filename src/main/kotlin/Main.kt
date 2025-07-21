package de.luca

val dishList = mutableListOf<Dish>()

fun main() {
    //TODO add ascII art
    Data.load()

    while(true) {
        printCommands()
        val input = readln()
        when(input) {
            "add" -> addDish()
            "remove" -> removeDish()
            "list" -> listDishes()
            "exit" -> {
                Data.save()
                println("Exiting")
                break
            }
        }
    }
}

fun addDish() {
    println("Enter the name of the dish:")
    val name = readln()
    if(dishList.contains(Dish(name))) return println("Dish already exists")
    else dishList.add(Dish(name))
}

fun removeDish() {
    println("Enter the name of the dish:")
    val name = readln()
    if(dishList.contains(Dish(name))) dishList.remove(Dish(name))
    else println("Dish does not exist in the list")
}

fun listDishes() {
    println("--- Dish List ---")
    for (dish in dishList) {
//        dish.printDish()
        println(dish.name)
    }
}

fun printCommands() {
    println("--- Commands ---")
    println("add: adds a new dish")
    println("remove: removes a dish")
    println("list: lists all dishes")
    println("exit: exits the program")
    println("-----------------")
}