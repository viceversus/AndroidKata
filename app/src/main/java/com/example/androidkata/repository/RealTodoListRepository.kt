package com.example.androidkata.repository

import com.example.androidkata.entities.Todo
import com.example.androidkata.entities.TodoDB
import com.example.androidkata.storage.TodoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class RealTodoListRepository @Inject constructor(
    val todoDao: TodoDao
): TodoListRepository {
    override suspend fun getTodos(): Flow<List<Todo>> =
        todoDao
            .getTodos()
            .map { it.map { it.toTodo() } }

    override suspend fun addTodo(todo: Todo) {
        todoDao.addTodo(
            TodoDB(
                UUID.randomUUID().toString(),
                todo.title,
                todo.isComplete
            )
        )
    }

    override suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo.toTodoDB())
    }

    fun TodoDB.toTodo(): Todo = Todo(id, title, isComplete)
    fun Todo.toTodoDB(): TodoDB = TodoDB(
        id ?: UUID.randomUUID().toString(),
        title,
        isComplete
    )
}