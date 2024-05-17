package com.flasshka.data.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.flasshka.data.db.entities.HabitEntity
import com.flasshka.data.db.entities.HabitEntity.Companion.toDbEntity
import com.flasshka.domain.entities.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DbImpl @Inject constructor() {

    private lateinit var dao: Dao

    fun init(context: Context) {
        dao = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "hbt_database23.db"
        )
            .allowMainThreadQueries()
            .build()
            .getDao()
    }

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
            with(habit.toDbEntity()) {
                dao.updateHabit(
                    uid = uid,
                    name = name,
                    description = description,
                    priority = priority,
                    type = type,
                    periodicityCount = periodicityCount,
                    periodicityDays = periodicityDays,
                    color = color,
                    date = date,
                    doneDates = doneDates
                )
            }
        }
    }

    fun getAllWithLiveData(): LiveData<List<HabitEntity>> = dao.getHabitsWithLiveData()
}