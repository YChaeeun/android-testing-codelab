package com.example.android.architecture.blueprints.todoapp

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.local.ToDoDatabase
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource
import kotlinx.coroutines.runBlocking

object ServiceLocator {
	// One of the downsides of using a service locator is that it is a "shared singleton".
	// 1. when the test finishes, need to reset the state of service locator
	// 2. cannot run tests in parallel

	private val lock = Any()

	private var database: ToDoDatabase? = null

	// value of volatile variable will never be cached, always read & write main memory
	// make sure volatile variable is always up-to-date & same to all executed threads
	@Volatile
	var tasksRepository: TasksRepository? = null
		@VisibleForTesting set // way to express that the reason the setter is pubic is because of testing

	@VisibleForTesting
	fun resetRepository() {
		synchronized(lock) {
			runBlocking {
				TasksRemoteDataSource.deleteAllTasks()
			}

			// Clear all data to avoid test pollution
			database?.apply {
				clearAllTables()
				close()
			}

			database = null
			tasksRepository = null
		}
	}

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