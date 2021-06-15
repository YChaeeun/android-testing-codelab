package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {
	@get:Rule
	var instantExecutorRule = InstantTaskExecutorRule()

	@Test
	fun addNewTask_setNewTaskEvent() {
		val taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

		taskViewModel.addNewTask()
	}

	@Test
	fun addNewTask_setsNewTaskEvent() {
		val taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

		val observer = Observer<Event<Unit>> {}
		try {
			taskViewModel.newTaskEvent.observeForever(observer)
			taskViewModel.addNewTask()

			val value = taskViewModel.newTaskEvent.value
			assertThat(value?.getContentIfNotHandled(), (not(nullValue())))
		} finally {
			taskViewModel.newTaskEvent.removeObserver(observer)
		}
	}

	@Test
	fun addNewTask_setsNewTaskEvent_new() {
		// GIVEN a fresh ViewModel
		val taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

		// WHEN adding a new Task
		taskViewModel.addNewTask()

		// THEN new task event is triggered
		val value = taskViewModel.newTaskEvent.getOrAwaitValue()
		
		assertThat(value.getContentIfNotHandled(), not(nullValue()))
	}
}