package com.cppdesigns.amazing_assignment

import com.cppdesigns.amazing_assignment.data.ApiService
import com.cppdesigns.amazing_assignment.data.TeacherRepository
import com.cppdesigns.amazing_assignment.screen.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {
    single { ApiService.instance() }
    single { TeacherRepository(get()) }
    viewModel { MainViewModel(get()) }
}