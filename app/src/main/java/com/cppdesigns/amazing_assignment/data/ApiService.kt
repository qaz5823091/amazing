package com.cppdesigns.amazing_assignment.data

import com.cppdesigns.amazing_assignment.data.models.TeacherTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("teachers/{teacherName}/schedule")
    suspend fun searchTeacherTime(
        @Path("teacherName") teacherName: String,
        @Query("started_at") startTime: String,
    ): TeacherTime

    companion object {
        private var apiService: ApiService? = null
        fun instance(): ApiService = apiService ?: Retrofit.Builder()
            .baseUrl("https://en.amazingtalker.com/v1/guest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}