package com.cppdesigns.amazing_assignment.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(MainViewState())
    val viewState: StateFlow<MainViewState> = _viewState.asStateFlow()

    init {
        updateWeek(0)
    }

    fun nextWeek() {
        updateWeek(1)
    }

    fun previousWeek() {
        updateWeek(-1)
    }

    private fun updateWeek(counter: Int) {
        _viewState.update {
            if (it.page + counter >= 0) {
                it.copy(page = it.page + counter)
            } else {
                it
            }
        }
        val page = _viewState.value.page
        val now = LocalDateTime.now().plusDays(page * WEEK_COUNT.toLong())
        val end = now.plusDays(WEEK_COUNT.toLong())
        val nowText = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val endText = end.format(DateTimeFormatter.ofPattern("MM-dd"))
        val list = List<Pair<String, String>>(WEEK_COUNT) {
            val current = now.plusDays(it.toLong())
            Pair(current.dayOfWeek.toString(), current.dayOfMonth.toString())
        }
        _viewState.update {
            it.copy(
                time = now,
                timeText = "$nowText - $endText",
                weeks = list
            )
        }
    }

    companion object {
        private const val WEEK_COUNT: Int = 7
    }
}