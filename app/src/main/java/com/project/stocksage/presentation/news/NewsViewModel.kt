package com.project.stocksage.presentation.news

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.stocksage.domain.repository.StockRepository
import com.project.stocksage.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel(){
    var state by mutableStateOf(NewsArticleState())

    init {
        getNews()
    }

    fun onEvent(event: NewsArticleEvent){
        when(event){
            NewsArticleEvent.Refresh -> {
                getNews(fetchFromRemote = true)
            }
        }

    }

    private fun getNews(
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository
                .getNewsArticle(fetchFromRemote)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { news ->
                                state = state.copy(
                                    news = news
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}