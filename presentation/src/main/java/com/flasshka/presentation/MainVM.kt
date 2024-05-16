package com.flasshka.presentation

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
import com.flasshka.data.DaggerDataComponent
import com.flasshka.data.api.RequestsImpl
import com.flasshka.data.db.DbImpl
import com.flasshka.domain.entities.Habit
import com.flasshka.presentation.habits.HabitListActionType
import com.flasshka.presentation.navigation.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset


class MainVM : ViewModel() {
    var habitNameFilter: String by mutableStateOf("")
        private set

    private val db: DbImpl = DaggerDataComponent.create().getDb()
    private val api: RequestsImpl = DaggerDataComponent.create().getApi()

    private val router: Router = DaggerPresentationComponent.create().getRouter()
    private val toast: Toast = DaggerPresentationComponent.create().getToast()

    var indexChosenFilterByHabitType: Int by mutableIntStateOf(0)
        private set

    var sortHabitsByDateFromOld: Boolean by mutableStateOf(false)
        private set


    private val habits = MutableLiveData<MutableState<List<Habit>>>(mutableStateOf(listOf()))

    fun initRouter(navController: NavController) {
        router.init(navController)
    }

    fun initDb(context: Context) {
        db.init(context)
    }

    fun initToast(context: Context) {
        toast.init(context)
    }

    fun getFirstOrNullWithId(uid: String): Habit? =
        habits.value?.value?.firstOrNull { it.uid == uid }

    fun getSortedAndFilteredHabitList(): List<Habit> {
        val chosenFilterByHabitType = Habit.Type.entries[indexChosenFilterByHabitType]

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
                var uid = api.putHabit(habit)
                var needConnection = false

                if (uid == "") {
                    uid = habit.hashCode().toString()
                    needConnection = true
                }

                if (habits.value?.value?.any { x -> x.uid == uid } == true) {
                    db.update(habit)
                } else {
                    db.addHabit(habit.copy(uid = uid))
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

            is HabitListActionType.UpdateFilter -> getUpdateHabitNameFilterAction(
                action.newValue
            )

            is HabitListActionType.SortByDate -> getSortHabitsByDateAction(
                action.fromNew
            )

            is HabitListActionType.CompleteHabit -> getCompleteHabitAction(
                action.habit
            )

            is HabitListActionType.DeleteHabit -> getDeleteHabitAction(
                action.habit
            )
        }
    }

    fun loadListOfHabits() {
        viewModelScope.launch {
            db.getAllWithLiveData().observeForever {
                habits.value?.value = it.map { entity -> entity.toHabit() }
            }

            withContext(Dispatchers.Default) {
                val habitsFromWeb = api.getHabits()

                delay(5000)
                habits.value?.value?.let { list ->
                    // Загружаем на сервер данные из локальной бд, которых нет на сервере
                    list.filter { habitsFromWeb.any { habit -> habit.uid == it.uid }.not() }
                        .forEach {
                            val res = api.putHabit(it.copy(uid = ""))
                            db.delete(it.uid)
                            db.addHabit(it.copy(uid = res))
                        }

                    // Добавляем в локальную бд данные с сервера, которых нет в бд
                    habitsFromWeb
                        .filter { habits.value?.value?.any { habit -> habit.uid == it.uid } == false }
                        .forEach { db.addHabit(it) }
                }
            }
        }
    }

    private suspend fun waitWebResponse(habit: Habit) {
        var uid = ""
        while (uid == "") {
            delay(20000)
            uid = api.putHabit(habit)
        }

        db.delete(habit.uid)
        db.addHabit(habit.copy(uid = uid))
    }

    private fun getUpdateHabitNameFilterAction(newFilter: String): () -> Unit =
        { habitNameFilter = newFilter }

    private fun getSortHabitsByDateAction(fromNew: Boolean): () -> Unit =
        { sortHabitsByDateFromOld = fromNew }

    private fun getChangeIndexOfChosenType(index: Int): () -> Unit =
        { indexChosenFilterByHabitType = index }

    private fun getDeleteHabitAction(habit: Habit): () -> Unit = {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                db.delete(habit.uid)
                api.deleteHabit(habit)
            }
        }
    }

    private fun getCompleteHabitAction(habit: Habit): () -> Unit = {
        val newHabit = habit.copy(
            doneDates = habit.doneDates + listOf(
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            )
        )

        val startFrom = LocalDateTime.now()
            .minusDays(newHabit.periodicity.days.toLong())
            .toEpochSecond(ZoneOffset.UTC)

        val remainsToComplete =
            newHabit.periodicity.count - 1 - newHabit.doneDates.count { it >= startFrom }

        if (newHabit.type == Habit.Type.GOOD) {
            showToast(
                remainsToComplete = remainsToComplete,
                textIfTrue = "Ты потрясающий!",
                textIfFalse = "Стоит выполнить еще $remainsToComplete раз"
            )
        } else {
            showToast(
                remainsToComplete = remainsToComplete,
                textIfTrue = "Хватит это делать!",
                textIfFalse = "Можете выполнить еще $remainsToComplete раз"
            )
        }

        completeHabit(newHabit)
    }

    private fun completeHabit(habit: Habit) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                db.update(habit)
                api.doneHabit(habit)
            }
        }
    }

    private fun showToast(remainsToComplete: Int, textIfTrue: String, textIfFalse: String) {
        if (remainsToComplete <= 0) {
            toast.showToast(textIfTrue)
        } else {
            toast.showToast(textIfFalse)
        }
    }
}