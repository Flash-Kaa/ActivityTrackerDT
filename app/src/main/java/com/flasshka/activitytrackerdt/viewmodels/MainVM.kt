package com.flasshka.activitytrackerdt.viewmodels

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.flasshka.activitytrackerdt.CreateHabitActivity
import com.flasshka.activitytrackerdt.Habit
import com.flasshka.activitytrackerdt.R
import com.flasshka.activitytrackerdt.models.Database
import com.flasshka.activitytrackerdt.views.HabitsListUI

class MainVM : ViewModel() {
    private val habits: List<Habit> by lazy {
        Database.habits
    }
    private val ui = HabitsListUI(this)

    fun toCreateActivity(activity: Activity) {
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
    fun DrawerList(activity: Activity) = ui.run {
        if (habits.isEmpty()) {
            EmptyList()
        } else {
            HabitsList(habits, activity)
        }

        CreateButton(activity = activity)
    }
}