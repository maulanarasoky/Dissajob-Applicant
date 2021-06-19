package org.d3ifcool.dissajobapplicant.data.source.repository.applicant

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalApplicantSource
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteApplicantSource
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.d3ifcool.dissajobapplicant.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class ApplicantRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteApplicantSource::class.java)
    private val local = mock(LocalApplicantSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val applicantRepository = FakeApplicantRepository(remote, local, appExecutors)

    private val applicantData = ApplicantDummy.generateApplicantData()

    @Test
    fun getApplicantDataTest() {
        val applicantLiveData = MutableLiveData<ApplicantEntity>()
        `when`(local.getApplicantData(applicantData.id)).thenReturn(applicantLiveData)
        applicantRepository.getApplicantData(applicantData.id)

        val applicantEntity = Resource.success(ApplicantDummy.generateApplicantData())
        verify(local).getApplicantData(applicantData.id)
        assertNotNull(applicantEntity.data)
        assertEquals(applicantData.id, applicantEntity.data?.id)
        assertEquals(applicantData.firstName, applicantEntity.data?.firstName)
        assertEquals(applicantData.lastName, applicantEntity.data?.lastName)
        assertEquals(applicantData.fullName, applicantEntity.data?.fullName)
        assertEquals(applicantData.email, applicantEntity.data?.email)
        assertEquals(applicantData.aboutMe, applicantEntity.data?.aboutMe)
        assertEquals(applicantData.phoneNumber, applicantEntity.data?.phoneNumber)
        assertEquals(applicantData.imagePath, applicantEntity.data?.imagePath)
    }
}