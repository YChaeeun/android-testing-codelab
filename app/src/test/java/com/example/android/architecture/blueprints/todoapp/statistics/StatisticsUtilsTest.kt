package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test

class StatisticsUtilsTest {

	@Test
	fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {
		val tasks = listOf<Task>(
			Task("title", "desc", isCompleted = false)
		)

		val result = getActiveAndCompletedStats(tasks)

		assertThat(result.completedTasksPercent, `is`(0f))
		assertThat(result.activeTasksPercent, `is`(100f))
	}

	@Test
	fun getActiveAndCompleteStats_noActive_returnsZeroHundreds() {
		val tasks = listOf<Task>(
			Task("title", "desc", isCompleted = true)
		)

		val result = getActiveAndCompletedStats(tasks)

		assertThat(result.activeTasksPercent, `is`(0f))
		assertThat(result.completedTasksPercent, `is`(100f))
	}

	@Test
	fun getActiveAndCompleteStats_both_returnsFortySixty() {
		val tasks = listOf(
			Task("title", "desc", isCompleted = true),
			Task("title", "desc", isCompleted = true),
			Task("title", "desc", isCompleted = true),
			Task("title", "desc", isCompleted = false),
			Task("title", "desc", isCompleted = false)
		)

		val result = getActiveAndCompletedStats(tasks)

		assertThat(result.activeTasksPercent, `is`(40f))
		assertThat(result.completedTasksPercent, `is`(60f))
	}

	@Test
	fun getActiveAndCompletedStats_error_returnsZeros() {
		// When there's an error loading stats
		val result = getActiveAndCompletedStats(null)

		// Both active and completed tasks are 0
		assertThat(result.activeTasksPercent, `is`(0f))
		assertThat(result.completedTasksPercent, `is`(0f))
	}

	@Test
	fun getActiveAndCompletedStats_empty_returnsZeros() {
		// When there are no tasks
		val result = getActiveAndCompletedStats(emptyList())

		// Both active and completed tasks are 0
		assertThat(result.activeTasksPercent, `is`(0f))
		assertThat(result.completedTasksPercent, `is`(0f))
	}
}