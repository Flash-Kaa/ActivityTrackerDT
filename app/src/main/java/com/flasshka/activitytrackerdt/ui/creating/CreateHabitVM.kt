package com.flasshka.activitytrackerdt.ui.creating

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.flasshka.activitytrackerdt.models.habit.Habit
import com.flasshka.activitytrackerdt.models.habit.HabitPeriodicity
import com.flasshka.activitytrackerdt.models.habit.HabitPriority
import com.flasshka.activitytrackerdt.models.habit.HabitType
import com.flasshka.activitytrackerdt.ui.MainVM
import com.flasshka.activitytrackerdt.ui.navigation.NavScreen
import java.time.LocalDateTime
import java.time.ZoneOffset

class FactoryForCreateVM(
    private val mainVM: MainVM
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateHabitVM(mainVM) as T
    }
}

class CreateHabitVM(private val vm: MainVM) : ViewModel() {
    private val allowedColorList = listOf(
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

    fun getHabitStateAction(id: Long?): () -> CreateHabitState {
        id?.let {
            vm.habits.firstOrNull { it.id == id }?.let {
                state = CreateHabitState(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    priority = it.priority,
                    type = it.type,
                    periodicityCount = it.periodicity.count.toString(),
                    periodicityDaysCount = it.periodicity.days.toString(),
                    color = it.color
                )

                return { state }
            }
        }

        val nextId = if (vm.habits.isEmpty()) 0L else vm.habits.maxOf { it.id } + 1

        state = CreateHabitState(id = nextId)

        return { state }
    }

    fun getAllowedColorAction(): () -> List<Color> = { allowedColorList }

    fun getNameIsCorrectAction(): () -> Boolean = { state.name.length > 3 }
    fun getPeriodicityCountIsCorrectAction(): () -> Boolean = {
        val res = state.periodicityCount.toIntOrNull()
        res != null && res > 0
    }

    fun getPeriodicityDaysCountIsCorrectAction(): () -> Boolean = {
        val res = state.periodicityDaysCount.toIntOrNull()
        res != null && res > 0
    }

    fun getSaveButtonIsEnabled(): () -> Boolean = {
        getNameIsCorrectAction().invoke()
                && getPeriodicityCountIsCorrectAction().invoke()
                && getPeriodicityDaysCountIsCorrectAction().invoke()
    }

    fun getOnNameChangeAction(): (String) -> Unit = { state = state.copy(name = it) }
    fun getOnDescriptionChangeAction(): (String) -> Unit = { state = state.copy(description = it) }
    fun getOnPriorityChangeAction(): (HabitPriority) -> Unit = { state = state.copy(priority = it) }
    fun getOnPeriodicityCountIsCorrectAction(): (String) -> Unit =
        { state = state.copy(periodicityCount = it) }

    fun getOnPeriodicityDaysCountIsCorrectAction(): (String) -> Unit =
        { state = state.copy(periodicityDaysCount = it) }

    fun getOnTypeChangeAction(): (HabitType) -> Unit = { state = state.copy(type = it) }
    fun getOnColorChangeAction(): (Color) -> Unit = { state = state.copy(color = it) }
    fun getOnSaveHabitAction(navController: NavController): () -> Unit = {
        val habit = Habit(
            id = state.id,
            name = state.name,
            description = state.description,
            priority = state.priority,
            type = state.type,
            periodicity = HabitPeriodicity(
                count = state.periodicityCount.toInt(),
                days = state.periodicityDaysCount.toInt()
            ),
            color = state.color,
            date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        )

        vm.addOrUpdate(habit)
        navController.navigate(NavScreen.MainScreen.route)
    }
}