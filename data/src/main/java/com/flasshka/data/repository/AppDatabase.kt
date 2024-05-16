package com.flasshka.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [com.flasshka.domain.entities.db.HabitEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
}