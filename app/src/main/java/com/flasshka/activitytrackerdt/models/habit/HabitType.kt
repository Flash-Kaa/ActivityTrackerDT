package com.flasshka.activitytrackerdt.models.habit

enum class HabitType {
    NEW_SKILL,
    DONT_FORGET,
    PRACTICE_MORE_OFTEN,
    AVOID;

    override fun toString() = when (this) {
        NEW_SKILL -> "Новый навык"
        DONT_FORGET -> "Не забыть"
        PRACTICE_MORE_OFTEN -> "Заниматься чаще"
        AVOID -> "Избегать"
    }
}
