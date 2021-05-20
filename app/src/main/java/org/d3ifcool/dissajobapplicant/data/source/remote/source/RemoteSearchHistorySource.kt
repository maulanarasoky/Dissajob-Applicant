package org.d3ifcool.dissajobapplicant.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.history.SearchHistoryResponseEntity
import org.d3ifcool.dissajobapplicant.ui.search.AddSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.search.DeleteSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.search.LoadSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.SearchHelper

class RemoteSearchHistorySource private constructor(
    private val searchHelper: SearchHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteSearchHistorySource? = null

        fun getInstance(searchHelper: SearchHelper): RemoteSearchHistorySource =
            instance ?: synchronized(this) {
                instance ?: RemoteSearchHistorySource(searchHelper)
            }
    }

    fun getSearchHistories(
        applicantId: String,
        callback: LoadSearchHistoryCallback
    ): LiveData<ApiResponse<List<SearchHistoryResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultHistory = MutableLiveData<ApiResponse<List<SearchHistoryResponseEntity>>>()
        searchHelper.getSearchHistories(applicantId, object : LoadSearchHistoryCallback {
            override fun onAllSearchHistoriesReceived(searchHistoryResponse: List<SearchHistoryResponseEntity>): List<SearchHistoryResponseEntity> {
                resultHistory.value =
                    ApiResponse.success(callback.onAllSearchHistoriesReceived(searchHistoryResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return searchHistoryResponse
            }
        })
        return resultHistory
    }

    fun addSearchHistory(
        searchHistory: SearchHistoryResponseEntity,
        callback: AddSearchHistoryCallback
    ) {
        EspressoIdlingResource.increment()
        searchHelper.addSearchHistory(searchHistory, object : AddSearchHistoryCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun deleteAllSearchHistories(
        applicantId: String,
        callback: DeleteSearchHistoryCallback
    ) {
        EspressoIdlingResource.increment()
        searchHelper.deleteAllSearchHistories(applicantId, object : DeleteSearchHistoryCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun deleteSearchHistoryById(
        searchHistoryId: String,
        callback: DeleteSearchHistoryCallback
    ) {
        EspressoIdlingResource.increment()
        searchHelper.deleteSearchHistoryById(searchHistoryId, object : DeleteSearchHistoryCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }
}