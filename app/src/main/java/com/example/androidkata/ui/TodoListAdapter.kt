package com.example.androidkata.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkata.R

class TodoListAdapter(
    var todos: List<Todo>,
    var onCheckChanged: (Int, Boolean) -> Unit
): RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {
    class TodoViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView
        val completed: CheckBox

        init {
            title = view.findViewById(R.id.todoTitle)
            completed = view.findViewById(R.id.todoComplete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_todo, parent, false)

        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todos[position]

        holder.title.text = todo.title
        holder.completed.isChecked = todo.completed
        holder.completed.setOnCheckedChangeListener { _, checked ->
            onCheckChanged(position, checked)
        }
    }

    override fun getItemCount(): Int = todos.size

    fun updateTodos(todos: List<Todo>) {
        this.todos = todos
        notifyDataSetChanged()
    }
}