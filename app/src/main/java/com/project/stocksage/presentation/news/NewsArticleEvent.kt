package com.project.stocksage.presentation.news

sealed class NewsArticleEvent {
    data object Refresh : NewsArticleEvent()
}