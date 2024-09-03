package com.project.stocksage.presentation.news

import com.project.stocksage.domain.model.NewsArticle

data class NewsArticleState (
    val news : List<NewsArticle> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)