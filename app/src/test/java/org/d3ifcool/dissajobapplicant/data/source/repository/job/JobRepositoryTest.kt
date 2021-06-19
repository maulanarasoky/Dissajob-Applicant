package org.d3ifcool.dissajobapplicant.data.source.repository.job

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.SavedJobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalJobSource
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.PagedListUtil
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.JobDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.RecruiterDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.SavedJobDummy
import org.d3ifcool.dissajobapplicant.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class JobRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteJobSource::class.java)
    private val local = mock(LocalJobSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val jobRepository = FakeJobRepository(remote, local, appExecutors)

    private val jobResponse = JobDummy.generateJobsData()
    private val jobDetailsResponse = JobDummy.generateJobDetails()
    private val savedJobResponse = SavedJobDummy.generateSavedJobsData()
    private val recruiterJobResponse = JobDummy.generateRecruiterJobsData()

    private val recruiterData = RecruiterDummy.generateRecruiterDetails()
    private val applicantData = ApplicantDummy.generateApplicantData()

    @Test
    fun getJobsTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, JobEntity>
        `when`(local.getJobs()).thenReturn(dataSourceFactory)
        jobRepository.getJobs()

        val jobEntities = Resource.success(PagedListUtil.mockPagedList(JobDummy.generateJobsData()))
        verify(local).getJobs()
        assertNotNull(jobEntities.data)
        assertEquals(jobResponse.size.toLong(), jobEntities.data?.size?.toLong())
    }

    @Test
    fun getSavedJobsTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, SavedJobEntity>
        `when`(local.getSavedJobs(applicantData.id)).thenReturn(dataSourceFactory)
        jobRepository.getSavedJobs(applicantData.id)

        val savedJobEntities =
            Resource.success(PagedListUtil.mockPagedList(SavedJobDummy.generateSavedJobsData()))
        verify(local).getSavedJobs(applicantData.id)
        assertNotNull(savedJobEntities.data)
        assertEquals(savedJobResponse.size.toLong(), savedJobEntities.data?.size?.toLong())
    }

    @Test
    fun getSavedJobByJobTest() {
        val savedJobLiveData = MutableLiveData<SavedJobEntity>()
        `when`(local.getSavedJobByJob(jobResponse[0].id, applicantData.id)).thenReturn(
            savedJobLiveData
        )
        jobRepository.getSavedJobByJob(jobResponse[0].id, applicantData.id)

        val savedJobEntity = Resource.success(SavedJobDummy.generateSavedJobsData())
        verify(local).getSavedJobByJob(jobResponse[0].id, applicantData.id)
        assertNotNull(savedJobEntity.data)
        assertEquals(savedJobResponse[0].id, savedJobEntity.data?.get(0)?.id)
        assertEquals(savedJobResponse[0].jobId, savedJobEntity.data?.get(0)?.jobId)
        assertEquals(savedJobResponse[0].applicantId, savedJobEntity.data?.get(0)?.applicantId)
    }

    @Test
    fun getJobByIdTest() {
        val jobLiveData = MutableLiveData<JobEntity>()
        `when`(local.getJobById(jobResponse[0].id)).thenReturn(jobLiveData)
        jobRepository.getJobById(jobResponse[0].id)

        val jobEntity = Resource.success(JobDummy.generateJobsData())
        verify(local).getJobById(jobResponse[0].id)
        assertNotNull(jobEntity.data)
        assertEquals(jobResponse[0].id, jobEntity.data?.get(0)?.id)
        assertEquals(jobResponse[0].title, jobEntity.data?.get(0)?.title)
        assertEquals(jobResponse[0].address, jobEntity.data?.get(0)?.address)
        assertEquals(jobResponse[0].postedBy, jobEntity.data?.get(0)?.postedBy)
        assertEquals(jobResponse[0].postedDate, jobEntity.data?.get(0)?.postedDate)
        assertEquals(jobResponse[0].isOpen, jobEntity.data?.get(0)?.isOpen)
        assertEquals(
            jobResponse[0].isOpenForDisability,
            jobEntity.data?.get(0)?.isOpenForDisability
        )
    }

    @Test
    fun getJobsByRecruiterTest() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, JobEntity>
        `when`(local.getJobsByRecruiter(recruiterData.id)).thenReturn(dataSourceFactory)
        jobRepository.getJobsByRecruiter(recruiterData.id)

        val jobEntities =
            Resource.success(PagedListUtil.mockPagedList(JobDummy.generateRecruiterJobsData()))
        verify(local).getJobsByRecruiter(recruiterData.id)
        assertNotNull(jobEntities.data)
        assertEquals(recruiterJobResponse.size.toLong(), jobEntities.data?.size?.toLong())
    }

    @Test
    fun getJobDetailsTest() {
        val jobDetailsLiveData = MutableLiveData<JobDetailsEntity>()
        `when`(local.getJobDetails(jobResponse[0].id)).thenReturn(jobDetailsLiveData)
        jobRepository.getJobDetails(jobResponse[0].id)

        val jobDetailsEntity = Resource.success(JobDummy.generateJobDetails())
        verify(local).getJobDetails(jobResponse[0].id)
        assertNotNull(jobDetailsEntity.data)
        assertEquals(jobDetailsResponse.id, jobDetailsEntity.data?.id)
        assertEquals(jobDetailsResponse.title, jobDetailsEntity.data?.title)
        assertEquals(jobDetailsResponse.description, jobDetailsEntity.data?.description)
        assertEquals(jobDetailsResponse.address, jobDetailsEntity.data?.address)
        assertEquals(jobDetailsResponse.qualification, jobDetailsEntity.data?.qualification)
        assertEquals(jobDetailsResponse.employment, jobDetailsEntity.data?.employment)
        assertEquals(jobDetailsResponse.type, jobDetailsEntity.data?.type)
        assertEquals(jobDetailsResponse.industry, jobDetailsEntity.data?.industry)
        assertEquals(jobDetailsResponse.salary, jobDetailsEntity.data?.salary)
        assertEquals(jobDetailsResponse.postedBy, jobDetailsEntity.data?.postedBy)
        assertEquals(jobDetailsResponse.postedDate, jobDetailsEntity.data?.postedDate)
        assertEquals(jobDetailsResponse.updatedDate, jobDetailsEntity.data?.updatedDate)
        assertEquals(jobDetailsResponse.closedDate, jobDetailsEntity.data?.closedDate)
        assertEquals(jobDetailsResponse.isOpen, jobDetailsEntity.data?.isOpen)
        assertEquals(
            jobDetailsResponse.isOpenForDisability,
            jobDetailsEntity.data?.isOpenForDisability
        )
        assertEquals(
            jobDetailsResponse.additionalInformation,
            jobDetailsEntity.data?.additionalInformation
        )
    }
}