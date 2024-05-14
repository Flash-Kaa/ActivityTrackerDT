package com.flasshka.activitytrackerdt.ui.creating

import androidx.compose.ui.graphics.Color
import com.flasshka.activitytrackerdt.models.habit.HabitPriority
import com.flasshka.activitytrackerdt.models.habit.HabitType

sealed class CreateHabitActionType {
    data object NavigateToListOfHabits : CreateHabitActionType()
    data class OnNameChange(val newName: String) : CreateHabitActionType()
    data class OnDescriptionChange(val newDescription: String) : CreateHabitActionType()
    data class OnPriorityChange(val newPriority: HabitPriority) : CreateHabitActionType()
    data class OnPeriodicityCountChange(val newValue: String) : CreateHabitActionType()
    data class OnPeriodicityDaysCountChange(val newValue: String) : CreateHabitActionType()
    data class OnTypeChange(val newType: HabitType) : CreateHabitActionType()
    data class OnColorChange(val newColor: Color) : CreateHabitActionType()
}