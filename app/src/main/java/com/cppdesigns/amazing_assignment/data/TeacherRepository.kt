package com.cppdesigns.amazing_assignment.data

import android.util.Log
import com.cppdesigns.amazing_assignment.data.models.TeacherTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TeacherRepository(
    private val apiService: ApiService
) {
    suspend fun getTimeTable(
        teacherName: String,
        time: LocalDateTime,
    ): TeacherTime {
        return try {
            val formatTime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
            apiService.searchTeacherTime(teacherName, formatTime!!)
        } catch (e: Exception) {
            TeacherTime()
        }
    }
}