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
import com.flasshka.activitytrackerdt.models.retrofit.request.Requests
import com.flasshka.activitytrackerdt.ui.habits.HabitListActionType
import com.flasshka.activitytrackerdt.ui.navigation.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainVM : ViewModel() {
    var habitNameFilter: String by mutableStateOf("")
        private set

    var indexChosenFilterByHabitType: Int by mutableIntStateOf(0)
        private set

    var sortHabitsByDateFromOld: Boolean by mutableStateOf(false)
        private set

    private lateinit var repository: Repository
    private lateinit var router: Router

    private val habits = MutableLiveData<MutableState<List<Habit>>>(mutableStateOf(listOf()))

    fun initRouter(navController: NavController) {
        router = Router(navController)
    }

    fun initRepository(applicationContext: Context) {
        repository = Repository(
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "hbt_database23.db")
                .allowMainThreadQueries()
                .build()
                .getDao()
        )

        loadListOfHabits()
    }

    fun getFirstOrNullWithId(uid: String): Habit? =
        habits.value?.value?.firstOrNull { it.uid == uid }

    fun getSortedAndFilteredHabitList(): List<Habit> {
        val chosenFilterByHabitType = HabitType.entries[indexChosenFilterByHabitType]

        return habits.value?.value?.let { list ->
            list.filter {
                it.type == chosenFilterByHabitType && it.name.contains(habitNameFilter, true)
            }.sortedBy {
                if (sortHabitsByDateFromOld) it.date else -it.date
            }
        } ?: emptyList()
    }

    fun addOrUpdate(habit: Habit) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                var uid = Requests.putHabit(habit)
                var needConnection = false

                if (uid == "") {
                    uid = habit.hashCode().toString()
                    needConnection = true
                }

                if (habits.value?.value?.any { x -> x.uid == uid } == true) {
                    repository.update(habit)
                } else {
                    repository.addHabit(habit.copy(uid = uid))
                }

                if (needConnection) {
                    waitWebResponse(habit.copy(uid = uid))
                }
            }
        }
    }

    fun getAction(action: HabitListActionType): () -> Unit {
        return when (action) {
            is HabitListActionType.NavigateToChangeHabit -> router.getNavigateToChangeHabit(
                action.uid
            )

            is HabitListActionType.NavigateToCreateHabit -> router.getNavigateToCreateHabit()
            is HabitListActionType.NavigateToInfo -> router.getNavigateToInfoAction()
            is HabitListActionType.NavigateToListOfHabits -> router.getNavigateToListOfHabits()
            is HabitListActionType.ChangeIndexOfChosenType -> getChangeIndexOfChosenType(
                action.newValue
            )

            is HabitListActionType.UpdateFilter -> getUpdateHabitNameFilterAction(action.newValue)
            is HabitListActionType.SortByDate -> getSortHabitsByDateAction(action.fromNew)
        }
    }

    private fun loadListOfHabits() {
        viewModelScope.launch {
            repository.getAllWithLiveData().observeForever {
                habits.value?.value = it.map { entity -> entity.toHabit() }
            }

            withContext(Dispatchers.Default) {
                val habitsFromWeb = Requests.getHabits()

                delay(5000)
                habits.value?.value?.let {  list ->
                    // Загружаем на сервер данные из локальной бд, которых нет на сервере
                    list.filter { habitsFromWeb.any { habit -> habit.uid == it.uid }.not() }
                        .forEach {
                            val res = Requests.putHabit(it.copy(uid = ""))
                            repository.delete(it.uid)
                            repository.addHabit(it.copy(uid = res))
                        }

                    // Добавляем в локальную бд данные с сервера, которых нет в бд
                    habitsFromWeb
                        .filter { habits.value?.value?.any { habit -> habit.uid == it.uid } == false }
                        .forEach { repository.addHabit(it) }
                }
            }
        }
    }

    private suspend fun waitWebResponse(habit: Habit) {
        var uid = ""
        while (uid == "") {
            delay(20000)
            uid = Requests.putHabit(habit)
        }

        repository.delete(habit.uid)
        repository.addHabit(habit.copy(uid = uid))
    }

    private fun getUpdateHabitNameFilterAction(newFilter: String): () -> Unit =
        { habitNameFilter = newFilter }

    private fun getSortHabitsByDateAction(fromNew: Boolean): () -> Unit =
        { sortHabitsByDateFromOld = fromNew }

    private fun getChangeIndexOfChosenType(index: Int): () -> Unit =
        { indexChosenFilterByHabitType = index }
}