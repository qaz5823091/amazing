package com.cppdesigns.amazing_assignment.screen

import java.time.LocalDateTime

data class MainViewState(
    var isLoading: Boolean = false,
    var teacherIndex: Int = 0,
    var page: Int = 0,
    var time: LocalDateTime = LocalDateTime.now(),
    var timeText: String = "",
    var weeks: List<Pair<String, String>> = emptyList(),
    var timeTable: Map<String, List<Pair<String, Boolean>>> = emptyMap(),
)