package com.flasshka.activitytrackerdt.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.flasshka.activitytrackerdt.models.database.AppDatabase
import com.flasshka.activitytrackerdt.models.database.Repository
import com.flasshka.activitytrackerdt.models.habit.Habit
import kotlinx.coroutines.launch

class MainVM : ViewModel() {
    private lateinit var repository: Repository;

    var habits: MutableList<Habit> by mutableStateOf(mutableListOf())

    fun initRepository(applicationContext: Context) {
        repository = Repository(
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
                .allowMainThreadQueries()
                .build()
                .getDao()
        )

        viewModelScope.launch {
            habits = repository.getAll().toMutableList()
        }
    }

    fun addOrUpdate(habit: Habit) {
        viewModelScope.launch {
            if (habits.any { x -> x.id == habit.id }) {
                repository.update(habit)
            } else {
                repository.addHabit(habit)
            }
            habits = repository.getAll().toMutableList()
        }
    }
}