package com.example.androidkata

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidkata.entities.Todo
import com.example.androidkata.repository.TodoListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TodoListState(
    val isAdding: Boolean = false,
    val todoList: List<Todo> = listOf()
)

sealed interface TodoAction {
    object ShowAddTodo: TodoAction
    object DismissAddTodo: TodoAction
    data class AddTodo(val title: String): TodoAction
    data class ToggleComplete(val todo: Todo): TodoAction
}

@HiltViewModel
class TodoListViewModel @Inject constructor(
    val repository: TodoListRepository
): ViewModel() {
    var state: TodoListState by mutableStateOf(TodoListState())
    private val actions = MutableSharedFlow<TodoAction>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        initialize()
    }

    fun performAction(action: TodoAction) {
        actions.tryEmit(action)
    }

    private fun initialize() {
        loadTodos()

        viewModelScope.launch {
            actions.collect {
                when(it) {
                    TodoAction.ShowAddTodo -> showAddTodo()
                    TodoAction.DismissAddTodo -> dismissAddTodo()
                    is TodoAction.AddTodo -> addTodo(it.title)
                    is TodoAction.ToggleComplete -> toggleComplete(it.todo)
                }
            }
        }
    }

    private fun loadTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTodos().collect {
                state = state.copy(todoList = it)
            }
        }
    }

    private fun dismissAddTodo() {
        state = state.copy(isAdding = false)
    }

    private fun showAddTodo() {
        state = state.copy(isAdding = true)
    }

    private fun addTodo(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(Todo(title = title))
            state = state.copy(isAdding = false)
        }
    }

    private fun toggleComplete(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todo.copy(isComplete = !todo.isComplete))
        }
    }
}