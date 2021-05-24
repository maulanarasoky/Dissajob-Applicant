package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.history.SearchHistoryEntity

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_histories WHERE applicant_id = :applicantId ORDER BY search_date DESC")
    fun getSearchHistories(applicantId: String): DataSource.Factory<Int, SearchHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchHistories(searchHistories: List<SearchHistoryEntity>)

    @Query("DELETE FROM search_histories WHERE id = :searchId")
    fun deleteSearchHistoryById(searchId: String)

    @Query("DELETE FROM search_histories WHERE applicant_id = :applicantId")
    fun deleteAllSearchHistory(applicantId: String)
}