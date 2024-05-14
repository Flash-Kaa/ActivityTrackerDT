package com.flasshka.activitytrackerdt.models.retrofit.response

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.flasshka.activitytrackerdt.models.habit.Habit
import com.flasshka.activitytrackerdt.models.habit.HabitPeriodicity
import com.flasshka.activitytrackerdt.models.habit.HabitPriority
import com.flasshka.activitytrackerdt.models.habit.HabitType

class HabitsListEntity : ArrayList<HabitsListEntity.HabitsListEntityItem>() {
    data class HabitsListEntityItem(
        val uid: String,
        val title: String,
        val description: String,
        val priority: Int,
        val type: Int,
        val count: Int,
        val frequency: Int,
        val color: Int,
        val date: Int,
        val done_dates: List<Int>
    ) {
        companion object {
            fun Habit.toEntity() = HabitsListEntityItem(
                uid = uid,
                title = name,
                description = description,
                priority = priority.ordinal,
                type = type.ordinal,
                count = periodicity.count,
                frequency = periodicity.days,
                color = color.toArgb(),
                date = date.toInt(),
                done_dates = doneDates.map { it.toInt() }
            )
        }

        fun toHabit() = Habit(
            uid = uid,
            name = title,
            description = description,
            priority = HabitPriority.entries[priority],
            type = HabitType.entries[type],
            periodicity = HabitPeriodicity(count, frequency),
            color = Color(color),
            date = date.toLong(),
            doneDates = done_dates.map { it.toLong() }
        )
    }

}