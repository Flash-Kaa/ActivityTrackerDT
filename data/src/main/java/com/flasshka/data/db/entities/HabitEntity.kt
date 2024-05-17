package com.flasshka.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flasshka.domain.entities.Habit

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
            color = color,
            date = date,
            doneDates = doneDates.joinToString(" ")
        )
    }

    fun toHabit() = Habit(
        uid = uid,
        name = name,
        description = description,
        priority = Habit.Priority.entries[priority],
        type = Habit.Type.entries[type],
        periodicity = Habit.Periodicity(periodicityCount, periodicityDays),
        color = color,
        date = date,
        doneDates = if (doneDates == "") listOf() else doneDates.split(" ").map { x -> x.toLong() }
    )
}
