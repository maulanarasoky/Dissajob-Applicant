package org.d3ifcool.dissajobapplicant.data.source.repository.history

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.history.SearchHistoryEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.history.SearchHistoryResponseEntity
import org.d3ifcool.dissajobapplicant.ui.search.callback.AddSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.search.callback.DeleteSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface SearchHistoryDataSource {
    fun getSearchHistories(applicantId: String): LiveData<Resource<PagedList<SearchHistoryEntity>>>
    fun addSearchHistory(
        searchHistory: SearchHistoryResponseEntity,
        callback: AddSearchHistoryCallback
    )
    fun deleteAllSearchHistories(
        applicantId: String,
        callback: DeleteSearchHistoryCallback
    )
    fun deleteSearchHistoryById(
        searchHistoryId: String,
        callback: DeleteSearchHistoryCallback
    )
}