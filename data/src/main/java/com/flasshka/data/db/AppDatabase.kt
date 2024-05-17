package com.flasshka.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flasshka.data.db.entities.HabitEntity

@Database(
    version = 1,
    entities = [HabitEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
}