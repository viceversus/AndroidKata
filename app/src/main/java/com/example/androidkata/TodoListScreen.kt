package com.example.androidkata

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TodoListScreen(
    state: TodoListState,
    actionHandler: (TodoListEvent) -> Unit
) {
    LazyColumn {
        itemsIndexed(state.todos) { i, todo ->
            Todo(i, todo, actionHandler)
        }
    }
}

@Composable
fun Todo(
    index: Int,
    todo: Todo,
    actionHandler: (TodoListEvent.Checked) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.completed,
            onCheckedChange = { checked -> actionHandler(TodoListEvent.Checked(index, checked)) }
        )
        Text(todo.title)
    }
}