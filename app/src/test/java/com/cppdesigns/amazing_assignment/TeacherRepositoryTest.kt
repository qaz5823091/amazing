package com.cppdesigns.amazing_assignment

import com.cppdesigns.amazing_assignment.data.ApiService
import com.cppdesigns.amazing_assignment.data.TeacherRepository
import com.cppdesigns.amazing_assignment.data.models.LocalDateTimeAdapter
import com.cppdesigns.amazing_assignment.data.models.TeacherTime
import com.cppdesigns.amazing_assignment.data.models.TimePeriod
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class TeacherRepositoryTest {
    @MockK
    private lateinit var apiService: ApiService
    private lateinit var teacherRepository: TeacherRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        teacherRepository = TeacherRepository(apiService)
        coEvery {
            apiService.searchTeacherTime(
                "name",
                "2025-06-29T07:30:00.000Z"
            )
        } returns ApiService.gson.fromJson(
            TestDataHelper.json,
            TeacherTime::class.java
        )
    }

    @Test
    fun testGetTimeTable() = runTest {
        val time = teacherRepository.getTimeTable(
            teacherName = "name",
            time = LocalDateTime.parse("2025-06-29T07:30:00Z", LocalDateTimeAdapter.formatter)
        )
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