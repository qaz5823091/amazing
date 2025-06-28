package com.cppdesigns.amazing_assignment.screen

import java.time.LocalDateTime

data class MainViewState(
    var time: LocalDateTime = LocalDateTime.now(),
    var timeText: String = "",
    var weeks: List<Pair<String, String>> = emptyList(),
    var page: Int = 0,
    var timeTable: Map<String, List<Pair<String, Boolean>>> = emptyMap(),
    var teacherName: String = "",
)