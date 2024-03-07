package com.flasshka.activitytrackerdt

import androidx.compose.ui.graphics.Color

data class Habit(
    var name: String,
    var description: String,
    var priority: HabitPriority,
    var type: HabitType,
    var periodicity: Periodicity,
    var color: Color
)