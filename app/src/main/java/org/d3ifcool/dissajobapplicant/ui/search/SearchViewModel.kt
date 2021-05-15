package org.d3ifcool.dissajobapplicant.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.history.SearchHistoryEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.history.SearchHistoryResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.history.SearchHistoryRepository
import org.d3ifcool.dissajobapplicant.vo.Resource

class SearchViewModel(private val searchHistoryRepository: SearchHistoryRepository) : ViewModel() {
    fun getSearchHistories(applicantId: String): LiveData<Resource<PagedList<SearchHistoryEntity>>> =
        searchHistoryRepository.getSearchHistories(applicantId)

    fun addSearchHistory(
        searchHistory: SearchHistoryResponseEntity,
        callback: AddSearchHistoryCallback
    ) = searchHistoryRepository.addSearchHistory(searchHistory, callback)
}