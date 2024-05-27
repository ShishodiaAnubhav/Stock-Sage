package com.project.stocksage.data.repository

import com.project.stocksage.data.local.StockDatabase
import com.project.stocksage.data.mapper.toCompanyListing
import com.project.stocksage.data.remote.StockApi
import com.project.stocksage.domain.model.CompanyListing
import com.project.stocksage.domain.repository.StockRepository
import com.project.stocksage.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepoImplementation @Inject constructor(
    val api: StockApi,
    db: StockDatabase
) : StockRepository {

    val dao = db.dao

    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListing = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListing.map { it.toCompanyListing() }
            ))

            val isDBEmpty = localListing.isEmpty() && query.isBlank()
            val shouldLoadFromCache = !isDBEmpty && !fetchFromRemote
            if (shouldLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListing = try {
                val resource = api.getListings()
                resource.byteStream()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }
        }
    }
}