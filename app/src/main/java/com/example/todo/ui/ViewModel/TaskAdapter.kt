package com.example.todo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.entity.Task
import com.example.todo.databinding.ItemLayoutBinding


class TaskAdapter (
          private val onTaskCheckedChange: (Task) -> Unit,
          private val onTaskDelete: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    inner class TaskViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply{
                taskTittle.text = task.title
                taskDescription.text = task.description
                taskCheckBox.isChecked = task.isCompleted

                taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    onTaskCheckedChange(task.copy(isCompleted = isChecked))
                }

                deleteButton.setOnClickListener {
                    onTaskDelete(task)

            }
        }
    }
}

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    }

    }