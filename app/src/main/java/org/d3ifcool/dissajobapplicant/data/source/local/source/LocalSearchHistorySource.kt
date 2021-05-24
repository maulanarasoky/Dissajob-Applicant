package org.d3ifcool.dissajobapplicant.data.source.local.source

import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.history.SearchHistoryEntity
import org.d3ifcool.dissajobapplicant.data.source.local.room.SearchHistoryDao

class LocalSearchHistorySource private constructor(
    private val mSearchHistoryDao: SearchHistoryDao
) {

    companion object {
        private var INSTANCE: LocalSearchHistorySource? = null

        fun getInstance(searchHistoryDao: SearchHistoryDao): LocalSearchHistorySource =
            INSTANCE ?: LocalSearchHistorySource(searchHistoryDao)
    }

    fun getSearchHistories(applicantId: String): DataSource.Factory<Int, SearchHistoryEntity> =
        mSearchHistoryDao.getSearchHistories(applicantId)

    fun insertSearchHistories(searchHistories: List<SearchHistoryEntity>) =
        mSearchHistoryDao.insertSearchHistories(searchHistories)

    fun deleteSearchHistoryById(searchId: String) =
        mSearchHistoryDao.deleteSearchHistoryById(searchId)

    fun deleteAllSearchHistories(applicantId: String) =
        mSearchHistoryDao.deleteAllSearchHistory(applicantId)
}