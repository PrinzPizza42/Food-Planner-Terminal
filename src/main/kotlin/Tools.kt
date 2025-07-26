package de.luca

import java.util.*


object Tools {
    fun getInt(questionWhenNotWorked: String?, minValue: Int, maxValue: Int): Int {
        return getEditedInt(questionWhenNotWorked, false, minValue, maxValue)
    }

    fun getEditedInt(
        questionWhenNotWorked: String?,
        acceptEnter: Boolean,
        minValue: Int,
        maxValue: Int
    ): Int {
        var initiativeInput: Int
        while (true) {
            try {
                val unparsed: String = readln()
                if (acceptEnter && unparsed.isEmpty()) {
                    return minValue - 1
                }
                initiativeInput = unparsed.toInt()
                if ((initiativeInput <= maxValue && initiativeInput >= minValue)) {
                    return initiativeInput
                } else {
                    println(questionWhenNotWorked)
                }
            } catch (e: NumberFormatException) {
                println(questionWhenNotWorked)
                readln()
            }
        }
    }

    fun getString(questionWhenNotWorked: String?, maxLength: Int): String? {
        return getEditedString(questionWhenNotWorked, false, maxLength)
    }

    fun getEditedString(questionWhenNotWorked: String?, acceptEnter: Boolean, maxLength: Int): String? {
        var nameInput: String
        while (true) {
            try {
                nameInput = readln()
                if (nameInput.isEmpty() && acceptEnter) return null
                if (!nameInput.isEmpty() && nameInput.length <= maxLength) {
                    return nameInput
                } else {
                    println(questionWhenNotWorked)
                }
            } catch (e: InputMismatchException) {
                println(questionWhenNotWorked)
                readln()
            }
        }
    }
}