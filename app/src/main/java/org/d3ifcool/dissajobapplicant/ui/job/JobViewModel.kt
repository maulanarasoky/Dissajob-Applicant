package org.d3ifcool.dissajobapplicant.ui.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobapplicant.vo.Resource

class JobViewModel(private val jobRepository: JobRepository) : ViewModel() {
    fun getJobs(): LiveData<Resource<PagedList<JobEntity>>> = jobRepository.getJobs()

    fun getJobDetails(jobId: String): LiveData<Resource<JobDetailsEntity>> =
        jobRepository.getJobDetails(jobId)
}