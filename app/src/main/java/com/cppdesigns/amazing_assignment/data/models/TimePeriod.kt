package com.cppdesigns.amazing_assignment.data.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TimePeriod(
    @SerializedName("start")
    val startTime: Date,
    @SerializedName("end")
    val endTime: Date,
)
