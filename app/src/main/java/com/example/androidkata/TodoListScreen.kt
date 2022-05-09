package com.example.androidkata

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.androidkata.entities.Todo

@Composable
fun TodoListScreen(
    state: TodoListState,
    actionHandler: (TodoAction) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { actionHandler(TodoAction.ShowAddTodo) }
            ) {
                Icon(Icons.Default.Add, "Add Todo")
            }
        }
    ) {
        if (state.isAdding) {
            Dialog(
                onDismissRequest = { actionHandler(TodoAction.DismissAddTodo) }
            ) {
                var todoTitle: String by remember { mutableStateOf("") }
                Box(modifier = Modifier.padding(16.dp)) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextField(value = todoTitle, onValueChange = { todoTitle = it })
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { actionHandler(TodoAction.AddTodo(todoTitle)) },
                            enabled = todoTitle.isNotBlank()
                        ) {
                            Text(text = "Add Todo")
                        }
                    }
                }
            }
        }
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                state.todoList.filter { !it.isComplete }.forEach {
                    item { TodoItem(it, actionHandler) }
                }

                item { CompletedHeader() }

                state.todoList.filter { it.isComplete }.forEach {
                    item { TodoItem(it, actionHandler) }
                }
            }
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    actionHandler: (TodoAction) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
       Checkbox(
           checked = todo.isComplete,
           onCheckedChange = { actionHandler(TodoAction.ToggleComplete(todo)) }
       )
       Text(text = todo.title)
    }
}

@Composable
fun CompletedHeader() {
    Box(
        modifier = Modifier.background(color = Color.LightGray)
            .fillMaxWidth()
            .padding(12.dp, 4.dp)
    ) {
        Text(text = "Completed")
    }
}