package com.example.task_six_sqlite3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class TaskDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,
    DATABASE_VERSION)
    {

    companion object {
        const val DATABASE_NAME = "tasks.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${TaskContract.TaskEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTERGER PRIMARY KEY," +
                    "${TaskContract.TaskEntry.COLUMN_TITLE} TEXT)"
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${TaskContract.TaskEntry.TABLE_NAME}")
        onCreate(db)
    }
}

fun addTask(dbHelper: TaskDbHelper, title: String): Long {
    val db = dbHelper.writableDatabase

    val values = ContentValues().apply {
        put(TaskContract.TaskEntry.COLUMN_TITLE, title)
    }

    val id = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)
    db.close()
    return id
}

fun getAllTasks(dbHelper: TaskDbHelper): List<Task> {
    val db = dbHelper.readableDatabase
    val projection = arrayOf(BaseColumns._ID, TaskContract.TaskEntry.COLUMN_TITLE)

    val cursor = db.query(
        TaskContract.TaskEntry.TABLE_NAME,
        null, null, null, null, null, null
    )

    val tasks = mutableListOf<Task>()
    with(cursor) {
        while (moveToNext()) {
            val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
            val title = getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_TITLE))
            tasks.add(Task(id, title))
        }
    }
    cursor.close()
    db.close()
    return tasks
}

fun deleteTask(dbHelper: TaskDbHelper, id: Long) {
    val db = dbHelper.writableDatabase
    db.delete(TaskContract.TaskEntry.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf(id.toString()))
    db.close()
}