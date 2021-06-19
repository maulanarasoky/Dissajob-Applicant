package org.d3ifcool.dissajobapplicant.data.source.repository.history

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.history.SearchHistoryEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalSearchHistorySource
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteSearchHistorySource
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.PagedListUtil
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.SearchHistoryDummy
import org.d3ifcool.dissajobapplicant.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class SearchHistoryRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteSearchHistorySource::class.java)
    private val local = mock(LocalSearchHistorySource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val searchHistoryRepository = FakeSearchHistoryRepository(remote, local, appExecutors)
    private val searchHistoryResponse = SearchHistoryDummy.generateSearchHistoriesData()
    private val applicantData = ApplicantDummy.generateApplicantData()

    @Test
    fun getSearchHistoriesTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, SearchHistoryEntity>
        `when`(local.getSearchHistories(applicantData.id)).thenReturn(dataSourceFactory)
        searchHistoryRepository.getSearchHistories(applicantData.id)

        val experienceEntities =
            Resource.success(PagedListUtil.mockPagedList(SearchHistoryDummy.generateSearchHistoriesData()))
        verify(local).getSearchHistories(applicantData.id)
        assertNotNull(experienceEntities.data)
        assertEquals(searchHistoryResponse.size.toLong(), experienceEntities.data?.size?.toLong())
    }
}