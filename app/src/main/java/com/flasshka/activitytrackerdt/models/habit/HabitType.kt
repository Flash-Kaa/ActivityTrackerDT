package com.flasshka.activitytrackerdt.models.habit

enum class HabitType {
    BAD,
    GOOD;

    override fun toString() = when (this) {
        BAD -> "Плохо"
        GOOD -> "Хорошо"
    }
}
