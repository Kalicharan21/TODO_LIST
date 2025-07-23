package com.example.todo.data.entity

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false

)
