package com.flasshka.activitytrackerdt.models.habit

import androidx.compose.ui.graphics.Color

data class Habit(
    val id: Long,
    val name: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val periodicity: HabitPeriodicity,
    val color: Color,
    val date: Long
)