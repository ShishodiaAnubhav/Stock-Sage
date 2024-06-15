package com.project.stocksage.data.repository

import com.project.stocksage.data.csv.CSVParser
import com.project.stocksage.data.local.StockDatabase
import com.project.stocksage.data.mapper.toCompanyListing
import com.project.stocksage.data.mapper.toCompanyListingEntity
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
    private val api: StockApi,
    db: StockDatabase,
    private val companyListingParsing: CSVParser<CompanyListing>
) : StockRepository {

    private val dao = db.dao

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
                companyListingParsing.parse(resource.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            // Single source of truth i.e. getting data from single source
            // so we shouldn't pass data from api to ui
//             remoteListing?.let { listings->
//                 emit(Resource.Success(listings))
//                 emit(Resource.Loading(false))
//                 dao.clearCompanyListing()
//                 dao.insertCompanyListing(
//                     listings.map { it.toCompanyListingEntity() }
//                 )
//             }

            // so we will first save data in cache then use cache for ui
            remoteListing?.let { listings ->
                dao.clearCompanyListing()
                dao.insertCompanyListing(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
            }
        }
    }
}