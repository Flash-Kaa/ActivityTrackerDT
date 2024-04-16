package com.flasshka.activitytrackerdt.ui.creating

import androidx.compose.ui.graphics.Color
import com.flasshka.activitytrackerdt.models.habit.HabitPriority
import com.flasshka.activitytrackerdt.models.habit.HabitType

data class CreateHabitState(
    val id: Long = -1,
    val name: String = "",
    val description: String = "",
    val priority: HabitPriority = HabitPriority.NONURGENT_AND_IMPORTANT,
    val type: HabitType = HabitType.GOOD,
    val periodicityCount: String = "",
    val periodicityDaysCount: String = "",
    val color: Color = Color.White
)
