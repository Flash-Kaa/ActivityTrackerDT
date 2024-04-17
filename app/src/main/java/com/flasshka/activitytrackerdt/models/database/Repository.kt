package com.flasshka.activitytrackerdt.models.database

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.LiveData
import com.flasshka.activitytrackerdt.models.database.HabitEntity.Companion.toEntity
import com.flasshka.activitytrackerdt.models.habit.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val dao: Dao) {
    suspend fun addHabit(habit: Habit) {
        withContext(Dispatchers.IO) {
            dao.addHabit(habit.toEntity())
        }
    }

    suspend fun delete(id: Long) {
        withContext(Dispatchers.IO) {
            dao.deleteHabitById(id)
        }
    }

    suspend fun update(habit: Habit) {
        withContext(Dispatchers.IO) {
            with(habit) {
                dao.updateHabit(
                    id = id,
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

    suspend fun getAll() = withContext(Dispatchers.IO) {
        return@withContext dao.getHabits().map { it.toHabit() }
    }

    fun getAllWithLiveData(): LiveData<List<HabitEntity>> = dao.getHabitsWithLiveData()
}