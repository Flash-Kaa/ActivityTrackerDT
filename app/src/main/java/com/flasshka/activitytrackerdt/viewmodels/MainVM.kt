package com.flasshka.activitytrackerdt.viewmodels

import androidx.lifecycle.ViewModel
import com.flasshka.activitytrackerdt.models.Database
import com.flasshka.activitytrackerdt.models.habit.Habit

class MainVM : ViewModel() {
    val habits: MutableList<Habit> by lazy {
        Database.habits
    }

    fun addOrUpdate(habit: Habit) {
        val filterById = habits.filter { x ->
            x.id == habit.id
        }

        if (filterById.isNotEmpty()) {
            Database.habits.remove(filterById.first())
        }

        val id = if (habits.isEmpty()) 0 else Database.habits.last().id + 1

        Database.habits.add(habit.copy(id = id))
    }
}