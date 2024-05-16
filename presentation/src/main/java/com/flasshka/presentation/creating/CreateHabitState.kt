package com.flasshka.presentation.creating

import androidx.compose.ui.graphics.Color
import com.flasshka.domain.entities.Habit

data class CreateHabitState(
    val uid: String? = "",
    val name: String = "",
    val description: String = "",
    val priority: Habit.Priority = Habit.Priority.NONURGENT_AND_IMPORTANT,
    val type: Habit.Type = Habit.Type.GOOD,
    val periodicityCount: String = "",
    val periodicityDaysCount: String = "",
    val color: Color = Color.White
)
