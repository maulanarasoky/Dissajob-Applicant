package org.d3ifcool.dissajobapplicant.ui.education

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.education.EducationRepository
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
class EducationViewModelTest {
    private lateinit var viewModel: EducationViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var educationRepository: EducationRepository

    @Mock
    private lateinit var educationObserver: Observer<Resource<PagedList<EducationEntity>>>

    @Mock
    private lateinit var pagedList: PagedList<EducationEntity>

    private val applicantData = ApplicantDummy.generateApplicantData()

    @Before
    fun setUp() {
        viewModel = EducationViewModel(educationRepository)
    }

    @Test
    fun getEducationsDataTest() {
        val dummyEducations = Resource.success(pagedList)
        `when`(dummyEducations.data?.size).thenReturn(1)
        val educations = MutableLiveData<Resource<PagedList<EducationEntity>>>()
        educations.value = dummyEducations

        `when`(educationRepository.getApplicantEducations(applicantData.id)).thenReturn(educations)
        val educationEntities = viewModel.getApplicantEducations(applicantData.id).value?.data
        verify(educationRepository).getApplicantEducations(applicantData.id)
        assertNotNull(educationEntities)
        assertEquals(1, educationEntities?.size)

        viewModel.getApplicantEducations(applicantData.id).observeForever(educationObserver)
        verify(educationObserver).onChanged(dummyEducations)
    }
}