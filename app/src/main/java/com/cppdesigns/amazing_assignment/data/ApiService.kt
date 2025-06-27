package com.cppdesigns.amazing_assignment.data

import com.cppdesigns.amazing_assignment.data.models.LocalDateTimeAdapter
import com.cppdesigns.amazing_assignment.data.models.TeacherTime
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime

interface ApiService {
    @GET("teachers/{teacherName}/schedule")
    suspend fun searchTeacherTime(
        @Path("teacherName") teacherName: String,
        @Query("started_at") startTime: String,
    ): TeacherTime

    companion object {
        private var apiService: ApiService? = null
        private val gson: Gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()
        fun instance(): ApiService = apiService ?: Retrofit.Builder()
            .baseUrl("https://en.amazingtalker.com/v1/guest/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}