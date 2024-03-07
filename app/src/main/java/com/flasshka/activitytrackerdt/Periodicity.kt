package com.flasshka.activitytrackerdt

data class Periodicity(
    val count: Int,
    val days: Int
) {
    override fun toString(): String {
        val daysStr = if (days == 1) "day" else "days"
        return "$count times in $days $daysStr"
    }
}
