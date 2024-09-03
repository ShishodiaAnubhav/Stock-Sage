package com.project.stocksage.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.project.stocksage.data.local.NewsArticleEntity
import com.project.stocksage.data.remote.dto.NewsArticleDto
import com.project.stocksage.domain.model.NewsArticle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun NewsArticleDto.toNewsArticleEntity(): NewsArticleEntity {
    return NewsArticleEntity(
        title = title ?: "",
        url = url ?: "",
        timePublished = timePublished ?: "",
        author = author?.firstOrNull() ?: "",
        imageUrl = imageUrl ?: "https://g.foolcdn.com/editorial/images/788702/etf-on-top-of-stock-charts-tickers.jpg"
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun NewsArticleEntity.toNewsArticle(): NewsArticle {
    val pattern = "yyyyMMdd'T'HHmmss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale("yyyy:MM:dd"))
    val localDateTime = LocalDateTime.parse(timePublished, formatter)
    return NewsArticle(
        title = title,
        url = url,
        publishedAt = localDateTime,
        author = author,
        imageUrl = imageUrl
    )
}

