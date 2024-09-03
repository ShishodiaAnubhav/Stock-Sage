package com.project.stocksage.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsInfo(
        newsArticleEntity: List<NewsArticleEntity>
    )

    @Query("DELETE FROM newsarticleentity")
    suspend fun clearNewsInfo()

    @Query("""
        SELECT *
        FROM newsarticleentity
        Order BY timePublished DESC
    """)
    suspend fun getNewsInfo(): List<NewsArticleEntity>
}