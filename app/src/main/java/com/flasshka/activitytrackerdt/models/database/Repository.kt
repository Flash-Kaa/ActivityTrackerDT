package com.flasshka.activitytrackerdt.models.database

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.LiveData
import com.flasshka.activitytrackerdt.models.database.HabitEntity.Companion.toDbEntity
import com.flasshka.activitytrackerdt.models.habit.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val dao: Dao) {
    suspend fun addHabit(habit: Habit) {
        withContext(Dispatchers.Default) {
            dao.addHabit(habit.toDbEntity())
        }
    }

    suspend fun delete(uid: String) {
        withContext(Dispatchers.Default) {
            dao.deleteHabitById(uid)
        }
    }

    suspend fun update(habit: Habit) {
        withContext(Dispatchers.Default) {
            with(habit) {
                dao.updateHabit(
                    uid = uid,
                    name = name,
                    description = description,
                    priority = priority.ordinal,
                    type = type.ordinal,
                    periodicityCount = periodicity.count,
                    periodicityDays = periodicity.days,
                    color = color.toArgb(),
                    date = date
                )
            }
        }
    }

    fun getAllWithLiveData(): LiveData<List<HabitEntity>> = dao.getHabitsWithLiveData()
}