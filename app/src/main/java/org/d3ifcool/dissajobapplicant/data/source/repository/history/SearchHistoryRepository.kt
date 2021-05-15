package org.d3ifcool.dissajobapplicant.data.source.repository.history

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.NetworkBoundResource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.history.SearchHistoryEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalSearchHistorySource
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.history.SearchHistoryResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteSearchHistorySource
import org.d3ifcool.dissajobapplicant.ui.search.AddSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.search.DeleteSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.search.LoadSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.NetworkStateCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class SearchHistoryRepository private constructor(
    private val remoteSearchHistorySource: RemoteSearchHistorySource,
    private val localSearchHistorySource: LocalSearchHistorySource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) :
    SearchHistoryDataSource {

    companion object {
        @Volatile
        private var instance: SearchHistoryRepository? = null

        fun getInstance(
            remoteJob: RemoteSearchHistorySource,
            localJob: LocalSearchHistorySource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): SearchHistoryRepository =
            instance ?: synchronized(this) {
                instance ?: SearchHistoryRepository(
                    remoteJob,
                    localJob,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun getSearchHistories(applicantId: String): LiveData<Resource<PagedList<SearchHistoryEntity>>> {
        return object :
            NetworkBoundResource<PagedList<SearchHistoryEntity>, List<SearchHistoryResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<SearchHistoryEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localSearchHistorySource.getSearchHistories(applicantId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<SearchHistoryEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()
//                data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<SearchHistoryResponseEntity>>> =
                remoteSearchHistorySource.getSearchHistories(
                    applicantId,
                    object : LoadSearchHistoryCallback {
                        override fun onAllSearchHistoriesReceived(searchHistoryResponse: List<SearchHistoryResponseEntity>): List<SearchHistoryResponseEntity> {
                            return searchHistoryResponse
                        }
                    })

            public override fun saveCallResult(data: List<SearchHistoryResponseEntity>) {
                val searchHistoryList = ArrayList<SearchHistoryEntity>()
                for (response in data) {
                    val searchHistory = SearchHistoryEntity(
                        response.id.toString(),
                        response.searchText,
                        response.searchDate,
                        response.applicantId
                    )
                    searchHistoryList.add(searchHistory)
                }

                localSearchHistorySource.insertSearchHistories(searchHistoryList)
            }
        }.asLiveData()
    }

    override fun addSearchHistory(
        searchHistory: SearchHistoryResponseEntity,
        callback: AddSearchHistoryCallback
    ) =
        appExecutors.diskIO()
            .execute { remoteSearchHistorySource.addSearchHistory(searchHistory, callback) }

    override fun deleteAllSearchHistory(
        applicantId: String,
        callback: DeleteSearchHistoryCallback
    ) =
        appExecutors.diskIO()
            .execute { remoteSearchHistorySource.deleteAllSearchHistory(applicantId, callback) }

    override fun deleteSearchHistoryById(
        searchHistoryId: String,
        callback: DeleteSearchHistoryCallback
    ) =
        appExecutors.diskIO()
            .execute { remoteSearchHistorySource.deleteSearchHistoryById(searchHistoryId, callback) }
}