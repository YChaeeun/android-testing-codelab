package com.example.android.architecture.blueprints.todoapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// Fragments (at least the ones you'll be testing) are visual and make up the user interface
// Thus when testing fragments, you usually write instrumented tests, which live in the androidTest source set.

@ExperimentalCoroutinesApi
@MediumTest // Marks the test as a "medium run-time" integration test
@RunWith(AndroidJUnit4::class)  // Used in any class using AndroidX Test.
class TaskDetailFragmentTest {

	private lateinit var repository: TasksRepository

	@Before
	fun initRepository() {
		repository = FakeAndroidTestRepository()
		ServiceLocator.tasksRepository = repository
	}

	@After
	fun cleanupDb() = runBlockingTest {
		ServiceLocator.resetRepository()
	}

	// to Run instrumental test, target device or emulator is needed
	@Test
	fun activeTaskDetails_DisplayedInUi() = runBlockingTest {
		// GIVEN
		val activeTask = Task("Active Task", "AndroidX Rocks", true)
		repository.saveTask(activeTask)

		// WHEN
		val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle() // fragment arguments
		launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)  // create FragmentScenario
		// theme is needed, because fragments usually get their themes from parent activity,
		// but in Test cases, your fragment is launched inside a generic empty activity so that it's properly isolated from activity code

		// THEN
		// check if shown UI is correct
		onView(withId(R.id.task_detail_title_text)).check(matches(isDisplayed()))
		onView(withId(R.id.task_detail_title_text)).check(matches(withText("Active Task")))

		onView(withId(R.id.task_detail_description_text)).check(matches(isDisplayed()))
		onView(withId(R.id.task_detail_description_text)).check(matches(withText("AndroidX Rocks")))

		onView(withId(R.id.task_detail_complete_checkbox)).check(matches(isDisplayed()))
//		onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))
		onView(withId(R.id.task_detail_complete_checkbox)).check(matches(isChecked()))

		Thread.sleep(2000)
	}
}