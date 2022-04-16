package com.example.androidkata.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkata.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val viewModel by viewModels<MainViewModel>()
    lateinit var rvTodoList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvTodoList = findViewById(R.id.rvTodoList)
        rvTodoList.adapter = TodoListAdapter(
            viewModel.state.value.todos,
            viewModel::onChecked
        )

        rvTodoList.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            viewModel.state.collect {
                updateViews(it.todos)
            }
        }
    }

    private fun updateViews(todoList: List<Todo>) {
        (rvTodoList.adapter as TodoListAdapter).updateTodos(todoList)
    }
}