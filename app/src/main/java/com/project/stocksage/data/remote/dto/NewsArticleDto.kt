package com.project.stocksage.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsResponseDto(
    @field:Json(name = "feed") val feed: List<NewsArticleDto>? = null
)

@JsonClass(generateAdapter = true)
data class NewsArticleDto (
    @field:Json(name = "title")val title: String?,
    @field:Json(name = "url")val url: String?,
    @field:Json(name = "time_published")val timePublished: String?,
    @field:Json(name = "banner_image")val imageUrl: String?,
    @field:Json(name = "authors")val author: List<String>?
)