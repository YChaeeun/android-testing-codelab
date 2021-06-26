package com.example.android.architecture.blueprints.todoapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.Test
import org.junit.runner.RunWith

// Fragments (at least the ones you'll be testing) are visual and make up the user interface
// Thus when testing fragments, you usually write instrumented tests, which live in the androidTest source set.

@MediumTest // Marks the test as a "medium run-time" integration test
@RunWith(AndroidJUnit4::class)  // Used in any class using AndroidX Test.
class TaskDetailFragmentTest {

	// to Run instrumental test, target device or emulator is needed
	@Test
	fun activeTaskDetails_DisplayedInUi() {
		// GIVEN
		val activeTask = Task("Active Task", "AndroidX Rocks", false)

		// WHEN
		val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle() // fragment arguments
		launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)  // create FragmentScenario
		// theme is needed, because fragments usually get their themes from parent activity,
		// but in Test cases, your fragment is launched inside a generic empty activity so that it's properly isolated from activity code

		Thread.sleep(2000)
	}
}