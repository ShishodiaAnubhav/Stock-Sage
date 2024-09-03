package com.project.stocksage.domain.model

import java.time.LocalDateTime

data class NewsArticle (
    val title: String,
    val url: String,
    val publishedAt: LocalDateTime,
    val imageUrl: String,
    val author: String
)