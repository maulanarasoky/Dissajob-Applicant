package org.d3ifcool.dissajobapplicant.ui.job

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.SavedJobEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicantDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.RecruiterDummy
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
class JobViewModelTest {
    private lateinit var viewModel: JobViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var jobRepository: JobRepository

    @Mock
    private lateinit var jobObserver: Observer<Resource<PagedList<JobEntity>>>

    @Mock
    private lateinit var savedJobObserver: Observer<Resource<PagedList<SavedJobEntity>>>

    @Mock
    private lateinit var jobPagedList: PagedList<JobEntity>

    @Mock
    private lateinit var savedJobPagedList: PagedList<SavedJobEntity>

    private val applicantData = ApplicantDummy.generateApplicantData()
    private val recruiterData = RecruiterDummy.generateRecruiterDetails()

    @Before
    fun setUp() {
        viewModel = JobViewModel(jobRepository)
    }

    @Test
    fun getJobsDataTest() {
        val dummyJobs = Resource.success(jobPagedList)
        `when`(dummyJobs.data?.size).thenReturn(1)
        val jobs = MutableLiveData<Resource<PagedList<JobEntity>>>()
        jobs.value = dummyJobs

        `when`(jobRepository.getJobs()).thenReturn(jobs)
        val jobEntities = viewModel.getJobs().value?.data
        verify(jobRepository).getJobs()
        assertNotNull(jobEntities)
        assertEquals(1, jobEntities?.size)

        viewModel.getJobs().observeForever(jobObserver)
        verify(jobObserver).onChanged(dummyJobs)
    }

    @Test
    fun getSavedJobsDataTest() {
        val dummySavedJobs = Resource.success(savedJobPagedList)
        `when`(dummySavedJobs.data?.size).thenReturn(1)
        val savedJobs = MutableLiveData<Resource<PagedList<SavedJobEntity>>>()
        savedJobs.value = dummySavedJobs

        `when`(jobRepository.getSavedJobs(applicantData.id)).thenReturn(savedJobs)
        val jobEntities = viewModel.getSavedJobs(applicantData.id).value?.data
        verify(jobRepository).getSavedJobs(applicantData.id)
        assertNotNull(jobEntities)
        assertEquals(1, jobEntities?.size)

        viewModel.getSavedJobs(applicantData.id).observeForever(savedJobObserver)
        verify(savedJobObserver).onChanged(dummySavedJobs)
    }

    @Test
    fun getRecruiterJobsDataTest() {
        val dummyJobs = Resource.success(jobPagedList)
        `when`(dummyJobs.data?.size).thenReturn(1)
        val recruiterJobs = MutableLiveData<Resource<PagedList<JobEntity>>>()
        recruiterJobs.value = dummyJobs

        `when`(jobRepository.getJobsByRecruiter(recruiterData.id)).thenReturn(recruiterJobs)
        val recruiterJobEntities = viewModel.getJobsByRecruiter(recruiterData.id).value?.data
        verify(jobRepository).getJobsByRecruiter(recruiterData.id)
        assertNotNull(recruiterJobEntities)
        assertEquals(1, recruiterJobEntities?.size)

        viewModel.getJobsByRecruiter(recruiterData.id).observeForever(jobObserver)
        verify(jobObserver).onChanged(dummyJobs)
    }
}