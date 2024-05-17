package com.flasshka.data.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.flasshka.data.api.entities.HabitDoneEntity
import com.flasshka.data.api.entities.HabitsListEntity.HabitsListEntityItem.Companion.toEntity
import com.flasshka.data.api.entities.IdBody
import com.flasshka.domain.entities.Habit
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import javax.inject.Inject

class RequestsImpl @Inject constructor() {
    companion object {
        val URL: String = "https://droid-test-server.doubletapp.ru/"
    }

    private val retrofit: HttpRequests = Retrofit.Builder()
        .baseUrl(RequestsImpl.URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HttpRequests::class.java)

    private val mapper: ObjectMapper = ObjectMapper()
    private val mediaType = MediaType.parse("application/json")

    fun getHabits(): List<Habit> {
        if (isInternetAvailable().not()) {
            return listOf()
        }

        val call = retrofit.getHabits()
        val response = call.execute()

        if (response.isSuccessful) {
            return response.body()?.map { it.toHabit() } ?: listOf()
        }

        return listOf()
    }

    fun putHabit(habit: Habit): String {
        if (isInternetAvailable().not()) {
            return ""
        }

        val value = mapper.writeValueAsString(habit.toEntity())
        val requestBody = RequestBody.create(mediaType, value)

        val call = retrofit.putHabit(requestBody)
        val response = call.execute()

        if (response.isSuccessful) {
            return response.body()?.uid ?: ""
        }

        return ""
    }

    fun deleteHabit(habit: Habit) {
        if (isInternetAvailable().not()) {
            return
        }

        val entity = habit.toEntity()

        val requestBody = RequestBody.create(
            mediaType,
            mapper.writeValueAsString(IdBody(entity.uid))
        )

        retrofit.deleteHabit(requestBody)
    }

    fun doneHabit(habit: Habit) {
        if (isInternetAvailable().not()) {
            return
        }

        val entity = habit.toEntity()

        val requestBody = RequestBody.create(
            mediaType,
            mapper.writeValueAsString(
                HabitDoneEntity(
                    entity.done_dates.last(),
                    entity.uid
                )
            )
        )

        retrofit.habitDone(requestBody)
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val ipAddress = InetAddress.getByName("google.com")
            ipAddress.hostAddress != null
        } catch (e: Exception) {
            false
        }
    }
}