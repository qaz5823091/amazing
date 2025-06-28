package com.cppdesigns.amazing_assignment

import com.cppdesigns.amazing_assignment.data.ApiService
import com.cppdesigns.amazing_assignment.data.models.LocalDateTimeAdapter
import com.cppdesigns.amazing_assignment.data.models.TimePeriod
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class ApiServiceTest {
    private lateinit var webServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        webServer = MockWebServer()
        webServer.start()
        apiService = ApiService.instance(webServer.url("/").toString())
    }

    @After
    fun tearDown() {
        webServer.shutdown()
    }

    @Test
    fun testTeacherTimeModel() = runTest {
        webServer.enqueue(MockResponse().setBody(TestDataHelper.json).setResponseCode(200))
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