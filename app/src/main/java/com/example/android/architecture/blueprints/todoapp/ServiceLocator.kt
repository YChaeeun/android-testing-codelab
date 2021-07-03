package com.example.android.architecture.blueprints.todoapp

import android.content.Context
import androidx.room.Room
import com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.local.ToDoDatabase
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource

object ServiceLocator {

	private var database: ToDoDatabase? = null

	// value of volatile variable will never be cached, always read & write main memory
	// make sure volatile variable is always up-to-date & same to all executed threads
	@Volatile
	var tasksRepository: TasksRepository? = null

	fun provideTasksRepository(context: Context): TasksRepository {
		synchronized(this) { // avoid creating more than one repository instance
			return tasksRepository ?: createTasksRepository(context)
		}
	}

	private fun createTasksRepository(context: Context): TasksRepository {
		val newRepo = DefaultTasksRepository(TasksRemoteDataSource, createTaskLocalDataSource(context))
		tasksRepository = newRepo
		return newRepo
	}

	private fun createTaskLocalDataSource(context: Context): TasksDataSource {
		val database = database ?: createDataBase(context)
		return TasksLocalDataSource(database.taskDao())
	}

	private fun createDataBase(context: Context): ToDoDatabase {
		val result = Room.databaseBuilder(
			context.applicationContext,
			ToDoDatabase::class.java, "Tasks.db"
		).build()

		database = result
		return result
	}
}