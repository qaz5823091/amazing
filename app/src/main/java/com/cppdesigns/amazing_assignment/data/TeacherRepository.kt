package com.cppdesigns.amazing_assignment.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.cppdesigns.amazing_assignment.data.models.TeacherTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TeacherRepository(
    private val apiService: ApiService
) {
    suspend fun getTimeTable(
        teacherName: String,
    ): TeacherTime {
        return try {
            val now = LocalDateTime.now()
            val formatTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
            apiService.searchTeacherTime(teacherName, formatTime)
        } catch (e: Exception) {
            Log.d("looog", e.toString())
            TeacherTime()
        }
    }
}