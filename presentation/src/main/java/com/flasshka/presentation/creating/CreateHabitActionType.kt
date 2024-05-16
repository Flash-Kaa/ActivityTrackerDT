package com.flasshka.presentation.creating

import androidx.compose.ui.graphics.Color
import com.flasshka.domain.entities.Habit

sealed class CreateHabitActionType {
    data object NavigateToListOfHabits : CreateHabitActionType()
    data class OnNameChange(val newName: String) : CreateHabitActionType()
    data class OnDescriptionChange(val newDescription: String) : CreateHabitActionType()
    data class OnPriorityChange(val newPriority: Habit.Priority) : CreateHabitActionType()
    data class OnPeriodicityCountChange(val newValue: String) : CreateHabitActionType()
    data class OnPeriodicityDaysCountChange(val newValue: String) : CreateHabitActionType()
    data class OnTypeChange(val newType: Habit.Type) : CreateHabitActionType()
    data class OnColorChange(val newColor: Color) : CreateHabitActionType()
}