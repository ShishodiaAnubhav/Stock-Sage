package com.project.stocksage.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toTimeAgo(): String {
    val now = LocalDateTime.now()
    val duration = Duration.between(this, now)

    return when {
        duration.toDays() > 0 -> {
            val days = duration.toDays()
            "$days day${if (days > 1) "s" else ""} ago"
        }
        duration.toHours() > 0 -> {
            val hours = duration.toHours()
            "$hours hour${if (hours > 1) "s" else ""} ago"
        }
        duration.toMinutes() > 0 -> {
            val minutes = duration.toMinutes()
            "$minutes minute${if (minutes > 1) "s" else ""} ago"
        }
        else -> "Just now"
    }
}