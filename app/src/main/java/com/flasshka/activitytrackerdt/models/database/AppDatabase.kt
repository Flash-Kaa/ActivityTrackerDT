package com.flasshka.activitytrackerdt.models.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [HabitEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
}