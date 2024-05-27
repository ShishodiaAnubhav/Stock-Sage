package com.project.stocksage.domain.repository

import com.project.stocksage.domain.model.CompanyListing
import com.project.stocksage.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}