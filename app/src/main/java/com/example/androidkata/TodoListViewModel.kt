package com.example.androidkata

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class TodoListState(val todos: List<Todo>)
sealed interface TodoListEvent {
    data class Checked(val index: Int, val checked: Boolean): TodoListEvent
}

class TodoListViewModel: ViewModel() {
    var state: TodoListState by mutableStateOf(TodoListState(listOf()))
    val events = MutableSharedFlow<TodoListEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
       state = TodoListState(listOf(
           Todo("New todo", false),
           Todo("Old Todo", true)
       ))

       viewModelScope.launch {
           handleActions()
       }
    }

    private suspend fun handleActions() {
        events.collect {
            when(it) {
                is TodoListEvent.Checked -> handleCheck(it.index, it.checked)
            }
        }
    }

    private fun handleCheck(index: Int, checked: Boolean) {
        val newTodos = state.todos.toMutableList()
        val newTodo = newTodos[index].copy(completed = checked)
        newTodos[index] = newTodo

        state = state.copy(todos = newTodos)
    }

    fun performAction(event: TodoListEvent) {
        events.tryEmit(event)
    }
}