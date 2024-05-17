package com.flasshka.presentation.creating

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flasshka.domain.entities.Habit
import com.flasshka.presentation.MainVM
import com.flasshka.presentation.habits.HabitListActionType
import java.time.LocalDateTime
import java.time.ZoneOffset

class FactoryForCreateVM(
    private val mainVM: MainVM
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateHabitVM(mainVM) as T
    }
}

class CreateHabitVM(private val mainVM: MainVM) : ViewModel() {
    val listOfAllowedColors = listOf(
        Color.Black,
        Color.Gray,
        Color(192, 192, 192),
        Color.White,
        Color(255, 0, 255),
        Color(128, 0, 128),
        Color.Red,
        Color(128, 0, 0),
        Color.Yellow,
        Color(128, 128, 0),
        Color(0, 255, 0),
        Color(0, 128, 0),
        Color(0, 255, 255),
        Color(0, 128, 128),
        Color.Blue,
        Color(0, 0, 128)
    )

    private var state: CreateHabitState by mutableStateOf(CreateHabitState())

    fun getHabitStateAction(uid: String?): () -> CreateHabitState {
        uid?.let {
            mainVM.getFirstOrNullWithId(uid)?.let {
                state = CreateHabitState(
                    uid = it.uid,
                    name = it.name,
                    description = it.description,
                    priority = it.priority,
                    type = it.type,
                    periodicityCount = it.periodicity.count.toString(),
                    periodicityDaysCount = it.periodicity.days.toString(),
                    color = Color(it.color)
                )

                return { state }
            }
        }

        state = CreateHabitState()

        return { state }
    }

    fun getAction(action: CreateHabitActionType): () -> Unit {
        return when (action) {
            is CreateHabitActionType.NavigateToListOfHabits -> ::getOnSaveHabitAction
            is CreateHabitActionType.OnColorChange -> getOnColorChangeAction(action.newColor)
            is CreateHabitActionType.OnTypeChange -> getOnTypeChangeAction(action.newType)
            is CreateHabitActionType.OnDescriptionChange -> getOnDescriptionChangeAction(action.newDescription)
            is CreateHabitActionType.OnNameChange -> getOnNameChangeAction(action.newName)
            is CreateHabitActionType.OnPeriodicityCountChange -> getOnPeriodicityCountChangeAction(
                action.newValue
            )

            is CreateHabitActionType.OnPeriodicityDaysCountChange -> getOnPeriodicityDaysCountChangeAction(
                action.newValue
            )

            is CreateHabitActionType.OnPriorityChange -> getOnPriorityChangeAction(action.newPriority)
        }
    }

    fun getNameIsCorrectAction(): Boolean = state.name.isNotEmpty()

    fun getPeriodicityCountIsCorrectAction(): Boolean {
        val res = state.periodicityCount.toIntOrNull()
        return res != null && res > 0
    }

    fun getPeriodicityDaysCountIsCorrectAction(): Boolean {
        val res = state.periodicityDaysCount.toIntOrNull()
        return res != null && res > 0
    }

    fun getDescriptionIsCorrectAction(): Boolean = state.description.isNotEmpty()

    fun getSaveButtonIsEnabled(): Boolean {
        return getNameIsCorrectAction()
                && getPeriodicityCountIsCorrectAction()
                && getPeriodicityDaysCountIsCorrectAction()
                && getDescriptionIsCorrectAction()
    }

    private fun getOnNameChangeAction(newName: String): () -> Unit =
        { state = state.copy(name = newName) }

    private fun getOnDescriptionChangeAction(newDescription: String): () -> Unit =
        { state = state.copy(description = newDescription) }

    private fun getOnPriorityChangeAction(newPriority: Habit.Priority): () -> Unit =
        { state = state.copy(priority = newPriority) }

    private fun getOnPeriodicityCountChangeAction(newValue: String): () -> Unit =
        { state = state.copy(periodicityCount = newValue) }

    private fun getOnPeriodicityDaysCountChangeAction(newValue: String): () -> Unit =
        { state = state.copy(periodicityDaysCount = newValue) }

    private fun getOnTypeChangeAction(newType: Habit.Type): () -> Unit =
        { state = state.copy(type = newType) }

    private fun getOnColorChangeAction(newColor: Color): () -> Unit =
        { state = state.copy(color = newColor) }

    private fun getOnSaveHabitAction() {
        val habit = Habit(
            uid = state.uid ?: "",
            name = state.name,
            description = state.description,
            priority = state.priority,
            type = state.type,
            periodicity = Habit.Periodicity(
                count = state.periodicityCount.toInt(),
                days = state.periodicityDaysCount.toInt()
            ),
            color = state.color.toArgb(),
            date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
            doneDates = listOf()
        )

        mainVM.addOrUpdate(habit)
        mainVM.getAction(HabitListActionType.NavigateToListOfHabits).invoke()
    }
}