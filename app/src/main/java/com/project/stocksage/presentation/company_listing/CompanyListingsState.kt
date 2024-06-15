package com.project.stocksage.presentation.company_listing

import com.project.stocksage.domain.model.CompanyListing

data class CompanyListingsState (
    val companies : List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)