package com.example.androidkata.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

data class Todo(
    val title: String,
    val completed: Boolean
)

data class State(val todos: List<Todo>)

class MainViewModel: ViewModel() {
    var state: MutableStateFlow<State> = MutableStateFlow(State(listOf()))

    init {
        state.tryEmit(
            State(listOf(
                Todo("New Todo", false),
                Todo("Old Todo", true)
            ))
        )
    }

    fun onChecked(index: Int, checked: Boolean) {
        val newTodos = state.value.todos.toMutableList()
        val newTodo = newTodos[index].copy(completed = checked)

        newTodos[index] = newTodo
        state.tryEmit(
            state.value.copy(todos = newTodos)
        )
    }
}