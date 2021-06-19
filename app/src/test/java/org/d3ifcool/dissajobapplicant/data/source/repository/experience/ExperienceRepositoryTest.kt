package org.d3ifcool.dissajobapplicant.data.source.repository.experience

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalExperienceSource
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteExperienceSource
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.PagedListUtil
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.ExperienceDummy
import org.d3ifcool.dissajobapplicant.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class ExperienceRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteExperienceSource::class.java)
    private val local = mock(LocalExperienceSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val experienceRepository = FakeExperienceRepository(remote, local, appExecutors)
    private val experienceResponse = ExperienceDummy.generateExperienceData()
    private val applicantData = ApplicantDummy.generateApplicantData()

    @Test
    fun getExperiencesTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ExperienceEntity>
        `when`(local.getApplicantExperiences(applicantData.id)).thenReturn(dataSourceFactory)
        experienceRepository.getApplicantExperiences(applicantData.id)

        val experienceEntities =
            Resource.success(PagedListUtil.mockPagedList(ExperienceDummy.generateExperienceData()))
        verify(local).getApplicantExperiences(applicantData.id)
        assertNotNull(experienceEntities.data)
        assertEquals(experienceResponse.size.toLong(), experienceEntities.data?.size?.toLong())
    }
}