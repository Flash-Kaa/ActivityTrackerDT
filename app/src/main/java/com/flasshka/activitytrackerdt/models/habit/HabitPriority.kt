package com.flasshka.activitytrackerdt.models.habit

enum class HabitPriority {
    URGENT_AND_IMPORTANT,
    URGENT_AND_UNIMPORTANT,
    NONURGENT_AND_IMPORTANT,
    NONURGENT_AND_UNIMPORTANT;

    override fun toString() = when (this) {
        URGENT_AND_IMPORTANT -> "Срочно и важно"
        URGENT_AND_UNIMPORTANT -> "Срочно и неважно"
        NONURGENT_AND_IMPORTANT -> "Несрочно, но важно"
        NONURGENT_AND_UNIMPORTANT -> "Несрочно и неважно"
    }
}