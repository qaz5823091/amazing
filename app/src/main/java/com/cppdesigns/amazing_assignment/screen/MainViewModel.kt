package com.cppdesigns.amazing_assignment.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cppdesigns.amazing_assignment.data.ApiService
import com.cppdesigns.amazing_assignment.data.TeacherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MainViewModel(
    private val teacherRepository: TeacherRepository = TeacherRepository(ApiService.instance())
) : ViewModel() {
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
        val now = if (page == 0) {
            LocalDateTime.now(ZoneOffset.UTC).plusDays(0)
        } else {
            val temp = LocalDateTime.now(ZoneOffset.UTC).plusDays(page * WEEK_COUNT.toLong())
            temp.withHour(0).withMinute(0).withSecond(0).withNano(0)
        }
        val end = now.plusDays(WEEK_COUNT.toLong())
        val nowText = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val endText = end.format(DateTimeFormatter.ofPattern("MM-dd"))
        val list = List(WEEK_COUNT) {
            val current = now.plusDays(it.toLong())
            Pair(current.dayOfWeek.toString(), current.dayOfMonth.toString())
        }
        val offset = ZoneId.systemDefault().rules.getOffset(Instant.now())
        _viewState.update {
            it.copy(
                time = now,
                timeText = "$nowText - $endText\n$offset",
                weeks = list
            )
        }
        fetchTimeTable()
    }

    private fun fetchTimeTable() {
        viewModelScope.launch {
            val result = teacherRepository.getTimeTable(
                teacherName = "sakurashimone",
                time = _viewState.value.time,
            )
            val map: MutableMap<String, MutableList<Pair<String, Boolean>>> = mutableMapOf()
            result.availableTime.forEach { time ->
                val zoneId = ZoneId.systemDefault()
                var current = time.startTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId)
                val endTime = time.endTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId)
                while (current < endTime) {
                    val key = current.format(DateTimeFormatter.ofPattern("dd"))
                    if (!map.containsKey(key)) {
                        map[key] = mutableListOf()
                    }
                    val text = current.format(DateTimeFormatter.ofPattern("HH:mm"))
                    map[key]?.add(Pair(text, true))
                    current = current.plusMinutes(30)
                }
            }
            result.bookedTime.forEach { time ->
                val zoneId = ZoneId.systemDefault()
                var current = time.startTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId)
                val endTime = time.endTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId)
                while (current < endTime) {
                    val key = current.format(DateTimeFormatter.ofPattern("dd"))
                    if (!map.containsKey(key)) {
                        map[key] = mutableListOf()
                    }
                    val text = current.format(DateTimeFormatter.ofPattern("HH:mm"))
                    map[key]?.add(Pair(text, false))
                    current = current.plusMinutes(30)
                }
            }
            map.forEach { item ->
                val value = item.value
                value.sortWith { a, b ->
                    val timeA = LocalTime.parse(a.first, DateTimeFormatter.ofPattern("HH:mm"))
                    val timeB = LocalTime.parse(b.first, DateTimeFormatter.ofPattern("HH:mm"))
                    timeA.compareTo(timeB)
                }
            }
            _viewState.update { it ->
                it.copy(timeTable = map)
            }
        }
    }

    companion object {
        private const val WEEK_COUNT: Int = 7
    }
}