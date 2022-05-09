package com.example.androidkata.repository

import com.example.androidkata.entities.Todo
import kotlinx.coroutines.flow.Flow

interface TodoListRepository {
    suspend fun getTodos(): Flow<List<Todo>>
    suspend fun addTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
}