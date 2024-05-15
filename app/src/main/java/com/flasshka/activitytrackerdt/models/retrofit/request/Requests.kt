package com.flasshka.activitytrackerdt.models.retrofit.request

import com.fasterxml.jackson.databind.ObjectMapper
import com.flasshka.activitytrackerdt.models.habit.Habit
import com.flasshka.activitytrackerdt.models.retrofit.RetrofitInstance
import com.flasshka.activitytrackerdt.models.retrofit.response.HabitsListEntity.HabitsListEntityItem.Companion.toEntity
import com.flasshka.activitytrackerdt.models.retrofit.response.IdBody
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.net.InetAddress


object Requests {
    private val mapper = ObjectMapper()
    private val client = OkHttpClient.Builder().build()
    private val retrofit = RetrofitInstance.get(client)
        .create(HttpRequests::class.java)

    suspend fun getHabits(): List<Habit> {
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

    suspend fun putHabit(habit: Habit): String {
        if (isInternetAvailable().not()) {
            return ""
        }

        val value = mapper.writeValueAsString(habit.toEntity())
        val requestBody = RequestBody.create(
            MediaType.parse("application/json"),
            value
        )

        val call = retrofit.putHabit(requestBody)
        val response = call.execute()

        if (response.isSuccessful) {
            return response.body()?.uid ?: ""
        }

        return ""
    }

    suspend fun deleteHabit(habit: Habit) {
        if (isInternetAvailable().not()) {
            return
        }

        val entity = habit.toEntity()

        val requestBody = RequestBody.create(
            MediaType.parse("application/json"),
            mapper.writeValueAsString(IdBody(entity.uid))
        )

        retrofit.deleteHabit(requestBody).execute()
    }

    suspend fun doneHabit(habit: Habit) {
        if (isInternetAvailable().not()) {
            return
        }

        val entity = habit.toEntity()

        val requestBody = RequestBody.create(
            MediaType.parse("application/json"),
            mapper.writeValueAsString(HabitDoneEntity(entity.done_dates.last(), entity.uid))
        )

        retrofit.habitDone(requestBody).execute()
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val ipAddr = InetAddress.getByName("google.com")
            ipAddr.hostAddress != null
        } catch (e: Exception) {
            false
        }
    }
}