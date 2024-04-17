package com.flasshka.activitytrackerdt.ui

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.room.Room
import com.flasshka.activitytrackerdt.models.database.AppDatabase
import com.flasshka.activitytrackerdt.models.database.Repository
import com.flasshka.activitytrackerdt.models.habit.Habit
import com.flasshka.activitytrackerdt.models.habit.HabitType
import com.flasshka.activitytrackerdt.ui.navigation.NavScreen
import kotlinx.coroutines.launch

class MainVM : ViewModel() {
    private lateinit var repository: Repository;

    private var habits = MutableLiveData<MutableState<List<Habit>>>(mutableStateOf(listOf()))

    private var habitNameFilter: String by mutableStateOf("")
    private var indexChosenFilterByHabitType: Int by mutableIntStateOf(0)
    private var sortHabitsByDateFromNew: Boolean by mutableStateOf(true)

    fun initRepository(applicationContext: Context) {
        repository = Repository(
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
                .allowMainThreadQueries()
                .build()
                .getDao()
        )

        viewModelScope.launch {
            repository.getAllWithLiveData().observeForever {
                habits.value?.value = it.map { entity -> entity.toHabit() }
            }
        }
    }

    fun getNextIndex(): Long {
        return habits.value?.value?.let {
            if (it.isEmpty()) null
            else it.maxOf { h -> h.id } + 1L
        } ?: 0L
    }

    fun getFirstOrNullWithId(id: Long): Habit? = habits.value?.value?.firstOrNull { it.id == id }

    fun getSortedAndFilteredHabitList(): () -> List<Habit> = {
        val chosenFilterByHabitType = HabitType.entries[indexChosenFilterByHabitType]

        habits.value?.value?.let { list ->
            list.filter {
                it.type == chosenFilterByHabitType && it.name.contains(habitNameFilter, true)
            }.sortedBy {
                if (sortHabitsByDateFromNew) it.date else -it.date
            }
        } ?: emptyList()
    }

    fun getCurrentValueHabitNameFilter(): () -> String = { habitNameFilter }
    fun getCurrentValueSortHabitsByDate(): () -> Boolean = { sortHabitsByDateFromNew }
    fun getCurrentIndexOfChosenFilterByHabitType(): () -> Int = { indexChosenFilterByHabitType }

    fun getUpdateHabitNameFilterAction(): (String) -> Unit = { habitNameFilter = it }

    fun getSortHabitsByDateAction(): (Boolean) -> (() -> Unit) {
        return { fromNew -> { sortHabitsByDateFromNew = fromNew } }
    }

    fun getChangeIndexOfChosenFilterByHabitTypeAction(): (Int) -> (() -> Unit) {
        return { ind -> { indexChosenFilterByHabitType = ind } }
    }

    fun getNavigateToChangeHabitAction(navController: NavController): (Long) -> (() -> Unit) {
        return { id -> { navController.navigate("${NavScreen.CreateHabitScreen.route}/${id}") } }
    }

    fun getNavigateToCreateHabitAction(navController: NavController): () -> Unit {
        return { navController.navigate(NavScreen.CreateHabitScreen.route) }
    }

    fun getNavigateToInfoAction(navController: NavController): () -> Unit {
        return { navController.navigate(NavScreen.MainScreen.InfoAboutApp.route) }
    }

    fun getNavigateToListOfHabitsAction(navController: NavController): () -> Unit {
        return { navController.navigate(NavScreen.MainScreen.ListOfHabits.route) }
    }

    fun addOrUpdate(habit: Habit) {
        viewModelScope.launch {
            if (habits.value?.value?.any { x -> x.id == habit.id } == true) {
                repository.update(habit)
            } else {
                repository.addHabit(habit)
            }
        }
    }
}