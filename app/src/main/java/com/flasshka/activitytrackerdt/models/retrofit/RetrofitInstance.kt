package com.flasshka.activitytrackerdt.models.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val url: String = "https://droid-test-server.doubletapp.ru/"

    fun get(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(url)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}