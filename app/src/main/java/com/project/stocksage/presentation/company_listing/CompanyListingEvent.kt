package com.project.stocksage.presentation.company_listing

sealed class CompanyListingEvent {
    data object Refresh : CompanyListingEvent()
    data class OnSearchQueryChanging(val query: String) : CompanyListingEvent()
}