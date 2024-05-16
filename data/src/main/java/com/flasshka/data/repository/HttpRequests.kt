package com.flasshka.data.repository

import com.flasshka.data.BuildConfig
import com.flasshka.domain.entities.api.HabitsListEntity
import com.flasshka.domain.entities.api.IdBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface HttpRequests {
    @Headers("accept: application/json", "Authorization: " + BuildConfig.API_KEY)
    @GET("/api/habit")
    fun getHabits(): Call<HabitsListEntity>

    @Headers(
        "accept: application/json",
        "Authorization: " + BuildConfig.API_KEY,
        "Content-Type: application/json"
    )
    @PUT("/api/habit")
    fun putHabit(@Body requestBody: RequestBody): Call<IdBody>

    @Headers(
        "accept: application/json",
        "Authorization: " + BuildConfig.API_KEY,
        "Content-Type: application/json"
    )
    @HTTP(method = "DELETE", path = "/api/habit", hasBody = true)
    fun deleteHabit(@Body requestBody: RequestBody): Call<ResponseBody>

    @Headers(
        "accept: application/json",
        "Authorization: " + BuildConfig.API_KEY,
        "Content-Type: application/json"
    )
    @POST("/api/habit_done")
    fun habitDone(@Body requestBody: RequestBody): Call<ResponseBody>
}