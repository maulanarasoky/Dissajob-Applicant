package org.d3ifcool.dissajobapplicant.ui.search

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.history.SearchHistoryResponseEntity

interface LoadSearchHistoryCallback {
    fun onAllSearchHistoriesReceived(searchHistoryResponse: List<SearchHistoryResponseEntity>): List<SearchHistoryResponseEntity>
}