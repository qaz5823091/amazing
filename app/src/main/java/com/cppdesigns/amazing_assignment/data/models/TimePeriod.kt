package com.cppdesigns.amazing_assignment.data.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class TimePeriod(
    @SerializedName("start")
    val startTime: LocalDateTime,
    @SerializedName("end")
    val endTime: LocalDateTime,
)
