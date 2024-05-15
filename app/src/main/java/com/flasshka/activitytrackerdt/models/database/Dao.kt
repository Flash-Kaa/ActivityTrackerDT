package com.flasshka.activitytrackerdt.models.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Insert(entity = HabitEntity::class)
    fun addHabit(entity: HabitEntity)

    @Query("DELETE FROM HabitEntity WHERE uid==:uid")
    fun deleteHabitById(uid: String)

    @Query(
        "UPDATE HabitEntity " +
                "SET name = :name, description = :description, priority = :priority, " +
                "type = :type, periodicityCount = :periodicityCount, " +
                "periodicityDays = :periodicityDays, color = :color, date = :date, " +
                "doneDates = :doneDates WHERE uid == :uid"
    )
    fun updateHabit(
        uid: String,
        name: String,
        description: String,
        priority: Int,
        type: Int,
        periodicityCount: Int,
        periodicityDays: Int,
        color: Int,
        date: Long,
        doneDates: String
    )

    @Query("SELECT * FROM HabitEntity")
    fun getHabits(): List<HabitEntity>

    @Query("SELECT * FROM HabitEntity")
    fun getHabitsWithLiveData(): LiveData<List<HabitEntity>>
}