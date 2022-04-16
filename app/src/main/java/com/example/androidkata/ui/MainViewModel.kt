package com.example.androidkata.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class Todo(
    val title: String,
    val completed: Boolean
)

data class State(val todos: List<Todo>)

sealed interface Event {
    object LoadNewData: Event
}

class MainViewModel: ViewModel() {
    var state: MutableStateFlow<State> = MutableStateFlow(State(listOf()))
    val events: MutableSharedFlow<Event> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        state.tryEmit(
            State(listOf(
                Todo("New Todo", false),
                Todo("Old Todo", true)
            ))
        )

        viewModelScope.launch {
            events.collect {
                when(it) {
                    Event.LoadNewData -> loadNew()
                }
            }
        }
    }

    fun performAction(action: Event) {
        events.tryEmit(action)
    }

    private fun loadNew() {
        state.tryEmit(
            State(listOf(
                Todo("LOADED NEW", false),
                Todo("SUPER NEW STUFF", true)
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