package com.flasshka.domain

import com.flasshka.domain.entities.Habit
import com.flasshka.domain.usecases.foreachIfNotContains
import com.flasshka.domain.usecases.getCurrentTime
import com.flasshka.domain.usecases.sortAndFilter
import org.junit.Test
import kotlin.random.Random

class ListOfHabitFunctionsTest {
    @Test
    fun `sort and filter random test`() {
        for (i in 10..<100) {
            val rand = Random(getCurrentTime())
            val list = generateRandomList(i)
            val type = Habit.Type.entries[rand.nextInt(2)]
            val name = generateName(2)
            val sortFromOld = rand.nextBoolean()

            val result = list.sortAndFilter(type, name, sortFromOld)

            result.forEach {
                assert(it.type == type)
                assert(it.name.contains(name, true))
            }

            for (j in 0..<result.size - 1) {
                assert(result[j].date < result[j + 1].date == sortFromOld)
            }
        }
    }

    @Test
    fun `sort and filter with empty list`() {
        for (i in 0..10) {
            val rand = Random(getCurrentTime())
            assert(
                listOf<Habit>().sortAndFilter(
                    Habit.Type.entries[rand.nextInt(2)],
                    "",
                    rand.nextBoolean()
                ).isEmpty()
            )
        }
    }

    @Test
    fun `forEach with empty list`() {
        val result = mutableListOf<Habit>()

        val first = listOf<Habit>()
        val second = generateRandomList(100)

        first.foreachIfNotContains(second) {
            result.add(it)
        }
        assert(result.isEmpty())

        second.foreachIfNotContains(first) {
            result.add(it)
        }
        assert(result.size == second.size)
    }

    @Test
    fun `forEach random test`() {
        for(i in 10..100) {
            val first = generateRandomList(i * 10, i)
            val second = generateRandomList(i * 10, i)

            first.foreachIfNotContains(second) {
                assert(second.any{h -> h.uid == it.uid}.not())
            }

            second.foreachIfNotContains(first) {
                assert(first.any{h -> h.uid == it.uid}.not())
            }
        }
    }

    private fun generateRandomList(size: Int, maxUid: Int = Int.MAX_VALUE): List<Habit> {
        val rand = Random(getCurrentTime())

        return List(size) {
            Habit(
                uid = rand.nextInt(maxUid).toString(),
                name = generateName(rand.nextInt(3, 6)),
                description = "description",
                priority = Habit.Priority.entries[rand.nextInt(3)],
                type = Habit.Type.entries[rand.nextInt(2)],
                periodicity = Habit.Periodicity(rand.nextInt(10), rand.nextInt(10)),
                color = rand.nextInt(),
                date = rand.nextLong(),
                doneDates = listOf()
            )
        }
    }

    private fun generateName(size: Int): String {
        val rand = Random(getCurrentTime())
        return List(size) { rand.nextInt(10) }.joinToString("")
    }
}