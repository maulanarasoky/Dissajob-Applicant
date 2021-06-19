package org.d3ifcool.dissajobapplicant.ui.media

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.media.MediaRepository
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.d3ifcool.dissajobapplicant.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MediaViewModelTest {
    private lateinit var viewModel: MediaViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mediaRepository: MediaRepository

    @Mock
    private lateinit var mediaObserver: Observer<Resource<PagedList<MediaEntity>>>

    @Mock
    private lateinit var pagedList: PagedList<MediaEntity>

    private val applicantData = ApplicantDummy.generateApplicantData()

    @Before
    fun setUp() {
        viewModel = MediaViewModel(mediaRepository)
    }

    @Test
    fun getMediaDataTest() {
        val dummyMedia = Resource.success(pagedList)
        `when`(dummyMedia.data?.size).thenReturn(1)
        val media = MutableLiveData<Resource<PagedList<MediaEntity>>>()
        media.value = dummyMedia

        `when`(mediaRepository.getApplicantMedia(applicantData.id)).thenReturn(media)
        val mediaEntities = viewModel.getApplicantMedia(applicantData.id).value?.data
        verify(mediaRepository).getApplicantMedia(applicantData.id)
        assertNotNull(mediaEntities)
        assertEquals(1, mediaEntities?.size)

        viewModel.getApplicantMedia(applicantData.id).observeForever(mediaObserver)
        verify(mediaObserver).onChanged(dummyMedia)
    }
}