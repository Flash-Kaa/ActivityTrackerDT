package com.flasshka.activitytrackerdt.viewmodels

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.flasshka.activitytrackerdt.Habit

class MainVM: ViewModel() {
    private val habits = mutableListOf<Habit>()

    fun addHabit(habit: Habit) {
        habits.add(habit)
    }

    @Composable
    fun DrawerList() {
        
    }
}