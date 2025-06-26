package com.cppdesigns.amazing_assignment.data.models

import com.google.gson.annotations.SerializedName

data class TeacherTime(
    @SerializedName("available")
    val availableTime: List<TimePeriod> = emptyList(),
    @SerializedName("booked")
    val bookedTime: List<TimePeriod> = emptyList(),
)
