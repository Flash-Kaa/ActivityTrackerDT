package com.flasshka.activitytrackerdt.viewmodels

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.flasshka.activitytrackerdt.Habit
import com.flasshka.activitytrackerdt.HabitPriority
import com.flasshka.activitytrackerdt.HabitType
import com.flasshka.activitytrackerdt.MainActivity
import com.flasshka.activitytrackerdt.Periodicity
import com.flasshka.activitytrackerdt.R
import com.flasshka.activitytrackerdt.models.Database
import com.flasshka.activitytrackerdt.views.CreateHabitUI

class CreateHabitVM : ViewModel() {
    var name: String by mutableStateOf("")
    var description: String by mutableStateOf("")
    var priority: HabitPriority by mutableStateOf(HabitPriority.URGENT_AND_IMPORTANT)
    var type: HabitType by mutableStateOf(HabitType.NEW_SKILL)

    var periodicityCount: Int by mutableIntStateOf(0)
    var periodicityDays: Int by mutableIntStateOf(0)
    var color: Color by mutableStateOf(Color.Black)

    private val ui: CreateHabitUI by lazy {
        CreateHabitUI(this)
    }

    @Composable
    fun Drawer(activity: Activity) {
        ui.Fields()
        ui.SaveButton(activity)
    }

    fun save(activity: Activity) {
        val habitToSave = Habit(
            name = name,
            description = description,
            priority = priority,
            type = type,
            periodicity = Periodicity(
                count = periodicityCount,
                days = periodicityDays
            ),
            color = color
        )

        Database.habits.add(habitToSave)
        toMainActivity(activity)
    }

    fun toMainActivity(activity: Activity) {
        Intent(activity, MainActivity::class.java).apply {
            activity.startActivity(this)
        }
    }

    fun loadFromIntent(intent: Intent, activity: Activity) {
        intent.getStringExtra(activity.getString(R.string.habit_name))?.let {
            name = it
        }

        intent.getStringExtra(activity.getString(R.string.habit_description))?.let {
            description = it
        }

        intent.getStringExtra(activity.getString(R.string.habit_priority))?.let {
            priority = HabitPriority.valueOf(it)
        }

        intent.getStringExtra(activity.getString(R.string.habit_type))?.let {
            type = HabitType.valueOf(it)
        }

        intent.getIntExtra(activity.getString(R.string.habit_periodicity_days), 0).let {
            periodicityDays = it
        }

        intent.getIntExtra(activity.getString(R.string.habit_periodicity_count), 0).let {
            periodicityCount = it
        }

        intent.getFloatExtra(activity.getString(R.string.habit_colorR), 0f).let { r ->
            intent.getFloatExtra(activity.getString(R.string.habit_colorG), 0f).let { g ->
                intent.getFloatExtra(activity.getString(R.string.habit_colorB), 0f).let { b ->
                    color = Color(r, g, b)
                }
            }
        }
    }
}