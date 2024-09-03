package com.project.stocksage.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val url: String,
    val timePublished: String,
    val imageUrl: String,
    val author: String
)