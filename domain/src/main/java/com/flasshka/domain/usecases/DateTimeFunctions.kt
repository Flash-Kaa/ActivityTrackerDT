package com.flasshka.domain.usecases

import android.annotation.SuppressLint
import java.time.LocalDateTime
import java.time.ZoneOffset

@SuppressLint("NewApi")
fun getCurrentTime(): Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)

@SuppressLint("NewApi")
fun getTimeMinusDays(daysCount: Int): Long =
    LocalDateTime.now().minusDays(daysCount.toLong()).toEpochSecond(
        ZoneOffset.UTC
    )

