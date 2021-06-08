package org.d3ifcool.dissajobapplicant.ui.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.SavedJobEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.SavedJobResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobapplicant.ui.job.callback.SaveJobCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.UnSaveJobCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class JobViewModel(private val jobRepository: JobRepository) : ViewModel() {
    fun getJobs(): LiveData<Resource<PagedList<JobEntity>>> = jobRepository.getJobs()

    fun getSavedJobs(applicantId: String): LiveData<Resource<PagedList<SavedJobEntity>>> =
        jobRepository.getSavedJobs(applicantId)

    fun getSavedJobByJob(jobId: String, applicantId: String): LiveData<Resource<SavedJobEntity>> =
        jobRepository.getSavedJobByJob(jobId, applicantId)

    fun getJobById(jobId: String): LiveData<Resource<JobEntity>> = jobRepository.getJobById(jobId)

    fun getJobsByRecruiter(recruiterId: String): LiveData<Resource<PagedList<JobEntity>>> =
        jobRepository.getJobsByRecruiter(recruiterId)

    fun getJobDetails(jobId: String): LiveData<Resource<JobDetailsEntity>> =
        jobRepository.getJobDetails(jobId)

    fun searchJob(searchText: String): LiveData<Resource<PagedList<JobEntity>>> =
        jobRepository.searchJob(searchText)

    fun getFilteredJobs(searchText: String): LiveData<PagedList<JobEntity>> =
        jobRepository.getFilteredJobs(searchText)

    fun saveJob(
        savedJob: SavedJobResponseEntity,
        callback: SaveJobCallback
    ) = jobRepository.saveJob(savedJob, callback)

    fun unSaveJob(
        id: String,
        callback: UnSaveJobCallback
    ) = jobRepository.unSaveJob(id, callback)
}