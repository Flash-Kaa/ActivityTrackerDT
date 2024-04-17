package com.flasshka.activitytrackerdt.models.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Insert(entity = HabitEntity::class)
    fun addHabit(entity: HabitEntity)

    @Query("DELETE FROM HabitEntity WHERE id=:id")
    fun deleteHabitById(id: Long)

    @Query(
        "UPDATE HabitEntity " +
                "SET name = :name, description = :description, priority = :priority, " +
                "type = :type, periodicityCount = :periodicityCount, " +
                "periodicityDays = :periodicityDays, color = :color, date = :date " +
                "WHERE id == :id"
    )
    fun updateHabit(
        id: Long,
        name: String,
        description: String,
        priority: Int,
        type: Int,
        periodicityCount: Int,
        periodicityDays: Int,
        color: Int,
        date: Long
    )

    @Query("SELECT * FROM HabitEntity")
    fun getHabits(): List<HabitEntity>

    @Query("SELECT * FROM HabitEntity")
    fun getHabitsWithLiveData(): LiveData<List<HabitEntity>>
}