package com.project.stocksage.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CompanyListingEntity::class, CompanyInfoEntity::class, NewsArticleEntity::class], version = 3, exportSchema = false)
abstract class StockDatabase: RoomDatabase() {
    abstract val stockDao:StockDao
    abstract val newsDao: NewsDao
}