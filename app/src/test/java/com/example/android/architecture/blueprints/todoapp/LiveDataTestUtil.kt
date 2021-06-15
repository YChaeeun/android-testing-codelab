package com.example.android.architecture.blueprints.todoapp

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


// https://medium.com/androiddevelopers/unit-testing-livedata-and-other-common-observability-problems-bb477262eb04
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
	time: Long = 2,
	timeUnit: TimeUnit = TimeUnit.SECONDS,
	afterObserve: () -> Unit = {}
): T {

	var data: T? = null
	val latch = CountDownLatch(1)

	// observe LiveData until it receives new Value
	val observer = object : Observer<T> {
		override fun onChanged(t: T) {
			data = t
			latch.countDown()

			// once value updated, remove observer
			this@getOrAwaitValue.removeObserver(this)
		}
	}

	this.observeForever(observer)

	try {
		afterObserve.invoke()

		// if the value is never set within 2 seconds, throws exception
		if (!latch.await(time, timeUnit)) {
			throw TimeoutException("LiveData Never Set")
		}
	} finally {
		this.removeObserver(observer)
	}

	@Suppress("UNCHECKED_CAST")
	return data as T
}