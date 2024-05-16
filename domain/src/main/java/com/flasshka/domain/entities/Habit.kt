package com.flasshka.domain.entities

import androidx.compose.ui.graphics.Color

data class Habit(
    val uid: String,
    val name: String,
    val description: String,
    val priority: Priority,
    val type: Type,
    val periodicity: Periodicity,
    val color: Color,
    val date: Long,
    val doneDates: List<Long>
) {
    data class Periodicity(
        val count: Int,
        val days: Int
    ) {
        override fun toString(): String {
            val daysStr = if (days == 1) "day" else "days"
            return "$count times in $days $daysStr"
        }
    }

    enum class Priority {
        URGENT_AND_IMPORTANT,
        URGENT_AND_UNIMPORTANT,
        NONURGENT_AND_IMPORTANT;

        override fun toString() = when (this) {
            URGENT_AND_IMPORTANT -> "Срочно и важно"
            URGENT_AND_UNIMPORTANT -> "Срочно и неважно"
            NONURGENT_AND_IMPORTANT -> "Несрочно, но важно"
        }
    }

    enum class Type {
        BAD,
        GOOD;

        override fun toString() = when (this) {
            BAD -> "Плохо"
            GOOD -> "Хорошо"
        }
    }
}