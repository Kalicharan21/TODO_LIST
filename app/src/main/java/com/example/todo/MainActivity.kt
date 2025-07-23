package com.example.todo

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.ui.TaskAdapter
import com.example.todo.ui.ViewModel.TaskViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.tasksRecyclerView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())


            view.updatePadding(

                top = insets.top,

                bottom = insets.bottom
            )


            WindowInsetsCompat.CONSUMED
        }

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskCheckedChange = { task ->
                taskViewModel.toggleTaskCompletion(task)
            },
            onTaskDelete = { task ->
                taskViewModel.deleteTask(task)
            }
        )
        binding.tasksRecyclerView.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        taskViewModel.allTasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks)
        }
    }

    private fun setupClickListeners() {
        binding.addTaskButton.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder.setTitle(getString(R.string.dialog_title))

        val dialogView = LayoutInflater.from(this).inflate(R.layout.add_item, null)
        dialogBuilder.setView(dialogView)

        val titleEdit = dialogView.findViewById<TextInputEditText>(R.id.titleEdit)
        val descriptionEdit = dialogView.findViewById<TextInputEditText>(R.id.descriptionEdit)

        dialogBuilder.setPositiveButton(getString(R.string.button_add)) { dialogInterface, _ ->
            val title = titleEdit.text.toString().trim()
            val description = descriptionEdit.text.toString().trim()

            if (title.isNotEmpty()) {
                taskViewModel.addTask(title, description)
                dialogInterface.dismiss()
            } else {
                titleEdit.error = getString(R.string.error_title_empty)
            }
        }
        dialogBuilder.setNegativeButton(getString(R.string.button_cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialogBuilder.create().show()
    }
}
