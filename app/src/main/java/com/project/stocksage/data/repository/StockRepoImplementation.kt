package com.project.stocksage.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.project.stocksage.data.csv.CSVParser
import com.project.stocksage.data.local.StockDatabase
import com.project.stocksage.data.mapper.toCompanyInfo
import com.project.stocksage.data.mapper.toCompanyInfoEntity
import com.project.stocksage.data.mapper.toCompanyListing
import com.project.stocksage.data.mapper.toCompanyListingEntity
import com.project.stocksage.data.mapper.toNewsArticle
import com.project.stocksage.data.mapper.toNewsArticleEntity
import com.project.stocksage.data.remote.StockApi
import com.project.stocksage.domain.model.CompanyInfo
import com.project.stocksage.domain.model.CompanyListing
import com.project.stocksage.domain.model.IntraDayInfo
import com.project.stocksage.domain.model.NewsArticle
import com.project.stocksage.domain.repository.StockRepository
import com.project.stocksage.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepoImplementation @Inject constructor(
    private val api: StockApi,
    db: StockDatabase,
    private val companyListingParsing: CSVParser<CompanyListing>,
    private val intraDayInfoParsing: CSVParser<IntraDayInfo>
) : StockRepository {

    private val stockDao = db.stockDao
    private val newsDao = db.newsDao

    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListing = stockDao.searchCompanyListing(query)
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
                stockDao.clearCompanyListing()
                stockDao.insertCompanyListing(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(
                    data = stockDao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): Resource<List<IntraDayInfo>> {
        return try {
            val response = api.getIntraDayInfo(symbol)
            val results = intraDayInfoParsing.parse(response.byteStream())
            Resource.Success(results)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't Load IntraDay info"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't Load IntraDay info"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Flow<Resource<CompanyInfo>> {
        return flow {

            emit(Resource.Loading(true))
            val localInfo = stockDao.getCompanyInformation(symbol)
            localInfo?.let {
                emit(Resource.Success(
                    data = it.toCompanyInfo()
                ))
            }

            val isDBEmpty = localInfo == null || localInfo.symbol.isEmpty()
            val shouldLoadFromCache = !isDBEmpty
            if (shouldLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteInfo = try {
                //val results =
                    api.getCompanyOverview(symbol)
                //Resource.Success(results.toCompanyInfoEntity())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(
                        message = "Couldn't Load Company info due to network error"
                    ))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(
                        message = "Couldn't Load Company info due to server error"
                    ))
                null
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                emit(Resource.Error(
                        message = "Illegal Argument provided"
                    ))
                null
            }
            remoteInfo?.let { info ->
                //dao.clearCompanyInfo()
                stockDao.insertCompanyInfo(
                    info.toCompanyInfoEntity()
                )
                val updatedInfo = stockDao.getCompanyInformation(symbol)

                updatedInfo?.let {
                    emit(Resource.Success(
                        data = it.toCompanyInfo()
                    ))
                }
                emit(Resource.Loading(false))
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getNewsArticle(fetchFromRemote: Boolean): Flow<Resource<List<NewsArticle>>> {
        return flow {
            emit(Resource.Loading(true))
            val localNews = newsDao.getNewsInfo()
            emit(Resource.Success(
                data = localNews.map { it.toNewsArticle() }
            ))

            val isDBEmpty = localNews.isEmpty()
            val shouldLoadFromCache = !isDBEmpty && !fetchFromRemote
            if (shouldLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteNews = try {
                //val results =
                api.getNewsInfo().feed
                //Resource.Success(results.toCompanyInfoEntity())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(
                    message = "Couldn't Load News due to network error"
                ))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(
                    message = "Couldn't News info due to server error"
                ))
                null
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                emit(Resource.Error(
                    message = "Illegal Argument provided"
                ))
                null
            }

            remoteNews?.let { news ->
                newsDao.clearNewsInfo()
                newsDao.insertNewsInfo(
                    news.map { it.toNewsArticleEntity() }
                )
                emit(Resource.Success(
                    data = newsDao.getNewsInfo()
                        .map { it.toNewsArticle() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }
}