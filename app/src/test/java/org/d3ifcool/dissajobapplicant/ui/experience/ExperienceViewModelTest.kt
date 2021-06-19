package org.d3ifcool.dissajobapplicant.ui.experience

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.experience.ExperienceRepository
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
class ExperienceViewModelTest {
    private lateinit var viewModel: ExperienceViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var experienceRepository: ExperienceRepository

    @Mock
    private lateinit var experienceObserver: Observer<Resource<PagedList<ExperienceEntity>>>

    @Mock
    private lateinit var pagedList: PagedList<ExperienceEntity>

    private val applicantData = ApplicantDummy.generateApplicantData()

    @Before
    fun setUp() {
        viewModel = ExperienceViewModel(experienceRepository)
    }

    @Test
    fun getExperiencesDataTest() {
        val dummyExperiences = Resource.success(pagedList)
        `when`(dummyExperiences.data?.size).thenReturn(1)
        val experiences = MutableLiveData<Resource<PagedList<ExperienceEntity>>>()
        experiences.value = dummyExperiences

        `when`(experienceRepository.getApplicantExperiences(applicantData.id)).thenReturn(
            experiences
        )
        val experienceEntities = viewModel.getApplicantExperiences(applicantData.id).value?.data
        verify(experienceRepository).getApplicantExperiences(applicantData.id)
        assertNotNull(experienceEntities)
        assertEquals(1, experienceEntities?.size)

        viewModel.getApplicantExperiences(applicantData.id).observeForever(experienceObserver)
        verify(experienceObserver).onChanged(dummyExperiences)
    }
}