package com.flasshka.activitytrackerdt.models.habit

data class HabitPeriodicity(
    val count: Int,
    val days: Int
) {
    override fun toString(): String {
        val daysStr = if (days == 1) "day" else "days"
        return "$count times in $days $daysStr"
    }
}