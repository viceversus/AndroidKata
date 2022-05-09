package com.example.androidkata.storage

import androidx.room.*
import com.example.androidkata.entities.TodoDB
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * from todoDB")
    fun getTodos(): Flow<List<TodoDB>>

    @Update
    fun updateTodo(todo: TodoDB)

    @Insert
    fun addTodo(todo: TodoDB)

    @Delete
    fun deleteTodo(todo: TodoDB)
}