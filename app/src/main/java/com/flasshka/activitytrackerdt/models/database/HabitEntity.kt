package com.flasshka.activitytrackerdt.models.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flasshka.activitytrackerdt.models.habit.Habit
import com.flasshka.activitytrackerdt.models.habit.HabitPeriodicity
import com.flasshka.activitytrackerdt.models.habit.HabitPriority
import com.flasshka.activitytrackerdt.models.habit.HabitType

@Entity
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val uid: String,
    val name: String,
    val description: String,
    val priority: Int,
    val type: Int,
    val periodicityCount: Int,
    val periodicityDays: Int,
    val color: Int,
    val date: Long,
    val doneDates: String
) {
    companion object {
        fun Habit.toDbEntity() = HabitEntity(
            uid = uid,
            name = name,
            description = description,
            priority = priority.ordinal,
            type = type.ordinal,
            periodicityCount = periodicity.count,
            periodicityDays = periodicity.days,
            color = color.toArgb(),
            date = date,
            doneDates = doneDates.joinToString(" ")
        )
    }

    fun toHabit() = Habit(
        uid = uid,
        name = name,
        description = description,
        priority = HabitPriority.entries[priority],
        type = HabitType.entries[type],
        periodicity = HabitPeriodicity(periodicityCount, periodicityDays),
        color = Color(color),
        date = date,
        doneDates = if (doneDates == "") listOf() else doneDates.split(" ").map { x -> x.toLong() }
    )
}
