package com.project.stocksage.di

import com.project.stocksage.data.csv.CSVParser
import com.project.stocksage.data.csv.CompanyListingParsing
import com.project.stocksage.data.csv.IntraDayInfoParser
import com.project.stocksage.data.repository.StockRepoImplementation
import com.project.stocksage.domain.model.CompanyListing
import com.project.stocksage.domain.model.IntraDayInfo
import com.project.stocksage.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule  {

    @Binds
    @Singleton
    abstract fun  bindCompanyListingParser(
        companyListingParser: CompanyListingParsing
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepoImplementation: StockRepoImplementation
    ): StockRepository

    @Binds
    @Singleton
    abstract fun  bindIntraDayInfoParser(
        intraDayInfoParser: IntraDayInfoParser
    ): CSVParser<IntraDayInfo>
}