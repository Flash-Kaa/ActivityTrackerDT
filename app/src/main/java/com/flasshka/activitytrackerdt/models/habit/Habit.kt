package com.flasshka.activitytrackerdt.models.habit

import androidx.compose.ui.graphics.Color

data class Habit(
    val uid: String,
    val name: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val periodicity: HabitPeriodicity,
    val color: Color,
    val date: Long,
    val doneDates: List<Long>
)