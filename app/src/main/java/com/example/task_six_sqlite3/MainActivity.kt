package com.example.task_six_sqlite3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.task_six_sqlite3.ui.theme.Task_six_sqlite3Theme

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: TaskDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dbHelper = TaskDbHelper(this@MainActivity)
        setContent {
            Task_six_sqlite3Theme {
                TaskApp(dbHelper)
            }
        }
    }
}

@Composable
fun TaskApp(dbHelper: TaskDbHelper) {
    var tasks by remember { mutableStateOf(emptyList<Task>()) }
    var newTaskTitle by remember { mutableStateOf("") }

    tasks = getAllTasks(dbHelper)
    Scaffold(
        modifier = Modifier.padding(16.dp)
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(
                value = newTaskTitle, onValueChange = { newTaskTitle = it },
                label = { Text(text = "Task Title") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    addTask(dbHelper, newTaskTitle)
                    tasks = getAllTasks(dbHelper)
                    newTaskTitle = ""
                }),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                addTask(dbHelper, newTaskTitle)
                tasks = getAllTasks(dbHelper)
                newTaskTitle = ""
            }) {
                Text(text = "Add Task")
            }
            TaskList(tasks, onDeleteTask = { id ->
                deleteTask(dbHelper, id)
                tasks = getAllTasks(dbHelper)
            })
        }
    }

}

@Composable
fun TaskList(tasks: List<Task>, onDeleteTask: (Long) -> Unit) {
    LazyColumn {
        items(tasks) { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = task.title, modifier = Modifier.weight(1f))
                IconButton(modifier = Modifier.weight(0.1f),
                onClick = { onDeleteTask(task.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val tasks = listOf<Task>(
        Task(1, "arroz"),
        Task(1, "arroz"),
        Task(1, "arroz"),
    )
    TaskList(tasks = tasks) {

    }
}