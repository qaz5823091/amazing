package com.cppdesigns.amazing_assignment

import com.cppdesigns.amazing_assignment.data.ApiService
import com.cppdesigns.amazing_assignment.data.models.LocalDateTimeAdapter
import com.cppdesigns.amazing_assignment.data.models.TeacherTime
import com.cppdesigns.amazing_assignment.data.models.TimePeriod
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class TeacherTimeTest {
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        apiService = mockk<ApiService>()
        coEvery { apiService.searchTeacherTime(any(), any()) } returns ApiService.gson.fromJson(
            TestDataHelper.json,
            TeacherTime::class.java
        )
    }

    @Test
    fun testTeacherTimeModel() = runTest {
        val time = apiService.searchTeacherTime("name", "2025-06-29T07:30:00Z")
        assertEquals(
            9,
            time.availableTime.size,
        )
        assertEquals(
            7,
            time.bookedTime.size,
        )
        assertEquals(
            TimePeriod(
                startTime = LocalDateTime.parse(
                    "2025-06-29T07:30:00Z",
                    LocalDateTimeAdapter.formatter
                ),
                endTime = LocalDateTime.parse(
                    "2025-06-29T10:30:00Z",
                    LocalDateTimeAdapter.formatter
                ),
            ),
            time.availableTime.first()
        )
        assertEquals(
            TimePeriod(
                startTime = LocalDateTime.parse(
                    "2025-06-29T10:30:00Z",
                    LocalDateTimeAdapter.formatter
                ),
                endTime = LocalDateTime.parse(
                    "2025-06-29T11:00:00Z",
                    LocalDateTimeAdapter.formatter
                ),
            ),
            time.bookedTime.first()
        )
        assertEquals(
            LocalDateTime.parse("2025-06-29T07:30:00Z", LocalDateTimeAdapter.formatter),
            time.availableTime.first().startTime
        )
        assertEquals(
            LocalDateTime.parse("2025-06-29T10:30:00Z", LocalDateTimeAdapter.formatter),
            time.availableTime.first().endTime
        )
        assertEquals(
            LocalDateTime.parse("2025-06-29T10:30:00Z", LocalDateTimeAdapter.formatter),
            time.bookedTime.first().startTime
        )
        assertEquals(
            LocalDateTime.parse("2025-06-29T11:00:00Z", LocalDateTimeAdapter.formatter),
            time.bookedTime.first().endTime
        )
    }
}