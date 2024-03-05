package com.flasshka.activitytrackerdt

import androidx.compose.ui.graphics.Color

data class Habit(
    val name: String,
    val description: String,
    val priority: Int,
    val type: HabitType,
    val periodicity: Periodicity,
    val color: Color
)
