package com.cppdesigns.amazing_assignment

import com.cppdesigns.amazing_assignment.data.ApiService
import com.cppdesigns.amazing_assignment.data.TeacherRepository
import com.cppdesigns.amazing_assignment.data.models.TeacherTime
import com.cppdesigns.amazing_assignment.screen.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    @MockK
    private lateinit var teacherRepository: TeacherRepository
    private lateinit var mainViewModel: MainViewModel
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mainViewModel = MainViewModel(teacherRepository)
        coEvery { teacherRepository.getTimeTable(any(), any()) } returns ApiService.gson.fromJson(
            TestDataHelper.json,
            TeacherTime::class.java
        )
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSelectTeacher() = runTest  {
        // 目前會噴錯，還沒有完全搞懂 Dispatcher 切換的解決方式
        mainViewModel.selectTeacher(0)
        val state = mainViewModel.viewState.value
        assertEquals(0, state.teacherIndex)
        assertEquals(0, state.page)
    }
}