package org.d3ifcool.dissajobapplicant.data.source.repository.application

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalApplicationSource
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteApplicationSource
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.PagedListUtil
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicationDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.JobDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.RecruiterDummy
import org.d3ifcool.dissajobapplicant.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class ApplicationRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteApplicationSource::class.java)
    private val local = mock(LocalApplicationSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val applicationRepository = FakeApplicationRepository(remote, local, appExecutors)

    private val applicationResponse = ApplicationDummy.generateApplicationsData()
    private val acceptedApplicationResponse = ApplicationDummy.generateAcceptedApplicationsData()
    private val rejectedApplicationResponse = ApplicationDummy.generateRejectedApplicationsData()
    private val markedApplicationResponse = ApplicationDummy.generateMarkedApplicationsData()

    private val recruiterData = RecruiterDummy.generateRecruiterDetails()
    private val jobData = JobDummy.generateJobsData()
    private val applicantData = ApplicantDummy.generateApplicantData()

    @Test
    fun getApplicationsTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ApplicationEntity>
        `when`(local.getApplications(recruiterData.id)).thenReturn(dataSourceFactory)
        applicationRepository.getApplications(recruiterData.id)

        val applicationEntities =
            Resource.success(PagedListUtil.mockPagedList(ApplicationDummy.generateApplicationsData()))
        verify(local).getApplications(recruiterData.id)
        assertNotNull(applicationEntities.data)
        assertEquals(applicationResponse.size.toLong(), applicationEntities.data?.size?.toLong())
    }

    @Test
    fun getAcceptedApplicationsTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ApplicationEntity>
        `when`(local.getAcceptedApplications(recruiterData.id)).thenReturn(dataSourceFactory)
        applicationRepository.getAcceptedApplications(recruiterData.id)

        val applicationEntities =
            Resource.success(PagedListUtil.mockPagedList(ApplicationDummy.generateApplicationsData()))
        verify(local).getAcceptedApplications(recruiterData.id)
        assertNotNull(applicationEntities.data)
        assertEquals(
            acceptedApplicationResponse.size.toLong(),
            applicationEntities.data?.size?.toLong()
        )
    }

    @Test
    fun getRejectedApplicationsTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ApplicationEntity>
        `when`(local.getRejectedApplications(recruiterData.id)).thenReturn(dataSourceFactory)
        applicationRepository.getRejectedApplications(recruiterData.id)

        val applicationEntities =
            Resource.success(PagedListUtil.mockPagedList(ApplicationDummy.generateApplicationsData()))
        verify(local).getRejectedApplications(recruiterData.id)
        assertNotNull(applicationEntities.data)
        assertEquals(
            rejectedApplicationResponse.size.toLong(),
            applicationEntities.data?.size?.toLong()
        )
    }

    @Test
    fun getMarkedApplicationsTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ApplicationEntity>
        `when`(local.getMarkedApplications(recruiterData.id)).thenReturn(dataSourceFactory)
        applicationRepository.getMarkedApplications(recruiterData.id)

        val applicationEntities =
            Resource.success(PagedListUtil.mockPagedList(ApplicationDummy.generateApplicationsData()))
        verify(local).getMarkedApplications(recruiterData.id)
        assertNotNull(applicationEntities.data)
        assertEquals(
            markedApplicationResponse.size.toLong(),
            applicationEntities.data?.size?.toLong()
        )
    }

    @Test
    fun getApplicationByJob() {
        val applicationLiveData = MutableLiveData<ApplicationEntity>()
        `when`(local.getApplicationByJob(jobData[0].id, applicantData.id)).thenReturn(
            applicationLiveData
        )
        applicationRepository.getApplicationByJob(jobData[0].id, applicantData.id)

        val applicationEntity = Resource.success(ApplicationDummy.generateApplicationsData())
        verify(local).getApplicationByJob(jobData[0].id, applicantData.id)
        assertNotNull(applicationEntity.data)
        assertEquals(applicationResponse[0].id, applicationEntity.data?.get(0)?.id)
        assertEquals(
            applicationResponse[0].applicantId,
            applicationEntity.data?.get(0)?.applicantId
        )
        assertEquals(applicationResponse[0].jobId, applicationEntity.data?.get(0)?.jobId)
        assertEquals(applicationResponse[0].applyDate, applicationEntity.data?.get(0)?.applyDate)
        assertEquals(
            applicationResponse[0].updatedDate,
            applicationEntity.data?.get(0)?.updatedDate
        )
        assertEquals(applicationResponse[0].status, applicationEntity.data?.get(0)?.status)
        assertEquals(applicationResponse[0].isMarked, applicationEntity.data?.get(0)?.isMarked)
        assertEquals(
            applicationResponse[0].recruiterId,
            applicationEntity.data?.get(0)?.recruiterId
        )
    }

    @Test
    fun getApplicationByIdTest() {
        val applicationLiveData = MutableLiveData<ApplicationEntity>()
        `when`(local.getApplicationById(applicationResponse[0].id)).thenReturn(applicationLiveData)
        applicationRepository.getApplicationById(applicationResponse[0].id)

        val applicationEntity = Resource.success(ApplicationDummy.generateApplicationsData())
        verify(local).getApplicationById(applicationResponse[0].id)
        assertNotNull(applicationEntity.data)
        assertEquals(applicationResponse[0].id, applicationEntity.data?.get(0)?.id)
        assertEquals(
            applicationResponse[0].applicantId,
            applicationEntity.data?.get(0)?.applicantId
        )
        assertEquals(applicationResponse[0].jobId, applicationEntity.data?.get(0)?.jobId)
        assertEquals(applicationResponse[0].applyDate, applicationEntity.data?.get(0)?.applyDate)
        assertEquals(
            applicationResponse[0].updatedDate,
            applicationEntity.data?.get(0)?.updatedDate
        )
        assertEquals(applicationResponse[0].status, applicationEntity.data?.get(0)?.status)
        assertEquals(applicationResponse[0].isMarked, applicationEntity.data?.get(0)?.isMarked)
        assertEquals(
            applicationResponse[0].recruiterId,
            applicationEntity.data?.get(0)?.recruiterId
        )
    }
}