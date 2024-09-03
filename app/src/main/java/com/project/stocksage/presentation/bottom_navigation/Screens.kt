package com.project.stocksage.presentation.bottom_navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens(val screen: String) {
    @Serializable
    data object CompanyListing: Screens("company_listing")
    @Serializable
    data object NewsScreen: Screens("news_screen")
    @Serializable
    data object CompanyInfo: Screens("company_info/{symbol}"){
        fun createRoute(symbol: String) = "company_info/$symbol"
    }
}