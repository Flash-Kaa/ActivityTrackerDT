package com.flasshka.domain.usecases

import com.flasshka.domain.entities.Habit

fun List<Habit>.sortAndFilter(
    type: Habit.Type,
    containInName: String,
    sortFromOld: Boolean
): List<Habit> {
    return filter {
        it.type == type && it.name.contains(containInName, true)
    }.sortedBy {
        if (sortFromOld) it.date else -it.date
    }
}

fun List<Habit>.foreachIfNotContains(
    other: List<Habit>,
    forEachAction: (Habit) -> Unit
) {
    filter { other.any { habit -> it.uid == habit.uid}.not() }
        .forEach(forEachAction)
}