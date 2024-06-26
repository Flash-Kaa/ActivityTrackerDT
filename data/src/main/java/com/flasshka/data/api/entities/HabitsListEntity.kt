package com.flasshka.data.api.entities

import com.flasshka.domain.entities.Habit

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
                color = color,
                date = date.toInt(),
                done_dates = doneDates.map { it.toInt() }
            )
        }

        fun toHabit() = Habit(
            uid = uid,
            name = title,
            description = description,
            priority = Habit.Priority.entries[priority],
            type = Habit.Type.entries[type],
            periodicity = Habit.Periodicity(count, frequency),
            color = color,
            date = date.toLong(),
            doneDates = done_dates.map { it.toLong() }
        )
    }

}