package com.flasshka.activitytrackerdt.ui.habits

sealed class HabitListActionType {
    data object NavigateToCreateHabit : HabitListActionType()
    data class NavigateToChangeHabit(val uid: String) : HabitListActionType()
    data object NavigateToInfo : HabitListActionType()
    data object NavigateToListOfHabits : HabitListActionType()
    data class UpdateFilter(val newValue: String) : HabitListActionType()
    data class SortByDate(val fromNew: Boolean) : HabitListActionType()
    data class ChangeIndexOfChosenType(val newValue: Int) : HabitListActionType()
}