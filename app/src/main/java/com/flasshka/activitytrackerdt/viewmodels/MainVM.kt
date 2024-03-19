package com.flasshka.activitytrackerdt.viewmodels

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import com.flasshka.activitytrackerdt.CreateHabitActivity
import com.flasshka.activitytrackerdt.Habit
import com.flasshka.activitytrackerdt.R
import com.flasshka.activitytrackerdt.models.Database
import com.flasshka.activitytrackerdt.ui.navigation.MainNavGraph

class MainVM : ViewModel() {
    val habits: List<Habit> by lazy {
        Database.habits
    }

    fun toCreateActivity(activity: Activity) {
        Log.i("my_log", "vm")
        Intent(activity, CreateHabitActivity::class.java).apply {
            activity.startActivity(this)
        }
    }

    fun toCreateActivity(activity: Activity, habit: Habit) {
        Database.habits.remove(habit)

        Intent(activity, CreateHabitActivity::class.java).apply {
            putExtra(activity.getString(R.string.habit_name), habit.name)
            putExtra(activity.getString(R.string.habit_description), habit.description)
            putExtra(activity.getString(R.string.habit_priority), habit.priority.name)
            putExtra(activity.getString(R.string.habit_type), habit.type.name)
            putExtra(activity.getString(R.string.habit_colorB), habit.color.blue)
            putExtra(activity.getString(R.string.habit_colorR), habit.color.red)
            putExtra(activity.getString(R.string.habit_colorG), habit.color.green)
            putExtra(activity.getString(R.string.habit_periodicity_count), habit.periodicity.count)
            putExtra(activity.getString(R.string.habit_periodicity_days), habit.periodicity.days)

            activity.startActivity(this)
        }
    }

    @Composable
    fun Navigation(activity: Activity) {
        val navController = rememberNavController()
        MainNavGraph(navController = navController, activity = activity, vm = this)
    }
}