package com.project.stocksage.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListing(
        companyListingEntity: List<CompanyListingEntity>
    )

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListing()

    @Query("""
        SELECT *
        FROM companylistingentity
        WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
        UPPER(:query) == symbol
    """)
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyInfo(
        companyInfoEntity: CompanyInfoEntity
    )

    @Query("DELETE FROM companyinfoentity")
    suspend fun clearCompanyInfo()

    @Query("""
        SELECT *
        FROM companyinfoentity
        WHERE symbol = :symbol
    """)
    suspend fun getCompanyInformation(symbol: String): CompanyInfoEntity?
}