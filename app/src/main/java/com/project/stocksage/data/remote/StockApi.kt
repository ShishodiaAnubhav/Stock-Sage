package com.project.stocksage.data.remote

import com.project.stocksage.data.remote.dto.CompanyInfoDto
import com.project.stocksage.data.remote.dto.NewsResponseDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntraDayInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyOverview(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): CompanyInfoDto

    @GET("query?function=NEWS_SENTIMENT&sort=LATEST")
    suspend fun getNewsInfo(
        @Query("apikey") apiKey: String = API_KEY
    ): NewsResponseDto

    companion object {
        const val API_KEY = "G5W42Q5YO7SHKVF4"
        const val BASE_URL = "https://www.alphavantage.co/"

    }
}