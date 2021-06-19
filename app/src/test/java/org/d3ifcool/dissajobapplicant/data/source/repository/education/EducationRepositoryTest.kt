package org.d3ifcool.dissajobapplicant.data.source.repository.education

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalEducationSource
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteEducationSource
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.PagedListUtil
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.EducationDummy
import org.d3ifcool.dissajobapplicant.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class EducationRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteEducationSource::class.java)
    private val local = mock(LocalEducationSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val educationRepository = FakeEducationRepository(remote, local, appExecutors)
    private val educationResponse = EducationDummy.generateEducationData()
    private val applicantData = ApplicantDummy.generateApplicantData()

    @Test
    fun getEducationsTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, EducationEntity>
        `when`(local.getApplicantEducations(applicantData.id)).thenReturn(dataSourceFactory)
        educationRepository.getApplicantEducations(applicantData.id)

        val educationEntities =
            Resource.success(PagedListUtil.mockPagedList(EducationDummy.generateEducationData()))
        verify(local).getApplicantEducations(applicantData.id)
        assertNotNull(educationEntities.data)
        assertEquals(educationResponse.size.toLong(), educationEntities.data?.size?.toLong())
    }
}