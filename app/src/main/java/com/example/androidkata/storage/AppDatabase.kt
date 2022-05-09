package com.example.androidkata.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidkata.entities.TodoDB

@Database(entities = [TodoDB::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}