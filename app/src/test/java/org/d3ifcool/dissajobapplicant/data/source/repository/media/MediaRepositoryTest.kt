package org.d3ifcool.dissajobapplicant.data.source.repository.media

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalMediaSource
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteMediaSource
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.PagedListUtil
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.MediaDummy
import org.d3ifcool.dissajobapplicant.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class MediaRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteMediaSource::class.java)
    private val local = mock(LocalMediaSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val mediaRepository = FakeMediaRepository(remote, local, appExecutors)

    private val mediaResponse = MediaDummy.generateMediaData()
    private val applicantData = ApplicantDummy.generateApplicantData()

    @Test
    fun getMediaTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MediaEntity>
        `when`(local.getApplicantMedia(applicantData.id)).thenReturn(dataSourceFactory)
        mediaRepository.getApplicantMedia(applicantData.id)

        val mediaEntities =
            Resource.success(PagedListUtil.mockPagedList(MediaDummy.generateMediaData()))
        verify(local).getApplicantMedia(applicantData.id)
        assertNotNull(mediaEntities.data)
        assertEquals(mediaResponse.size.toLong(), mediaEntities.data?.size?.toLong())
    }
}