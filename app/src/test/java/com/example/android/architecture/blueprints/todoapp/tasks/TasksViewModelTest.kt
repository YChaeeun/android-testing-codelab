package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {
	@get:Rule
	var instantExecutorRule = InstantTaskExecutorRule()

	private lateinit var tasksViewModel : TasksViewModel

	@Before
	fun setupViewModel() {
		tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
	}

	@Test
	fun addNewTask_setNewTaskEvent() {
		tasksViewModel.addNewTask()
	}

	@Test
	fun addNewTask_setsNewTaskEvent() {
		val observer = Observer<Event<Unit>> {}
		try {
			tasksViewModel.newTaskEvent.observeForever(observer)
			tasksViewModel.addNewTask()

			val value = tasksViewModel.newTaskEvent.value
			assertThat(value?.getContentIfNotHandled(), (not(nullValue())))
		} finally {
			tasksViewModel.newTaskEvent.removeObserver(observer)
		}
	}

	@Test
	fun addNewTask_setsNewTaskEvent_new() {
		// GIVEN a fresh ViewModel
//		val taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

		// WHEN adding a new Task
		tasksViewModel.addNewTask()

		// THEN new task event is triggered
		val value = tasksViewModel.newTaskEvent.getOrAwaitValue()

		assertThat(value.getContentIfNotHandled(), not(nullValue()))
	}

	@Test
	fun setFilterAllTasks_tasksAddViewVisible() {
		// GIVEN
//		val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

		// WHEN
		tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

		// THEN
		val value = tasksViewModel.tasksAddViewVisible.getOrAwaitValue()

		assertThat(value, `is`(true))
	}
}