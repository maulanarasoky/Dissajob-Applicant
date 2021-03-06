package org.d3ifcool.dissajobapplicant.data.source.repository.job

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.SavedJobEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.SavedJobResponseEntity
import org.d3ifcool.dissajobapplicant.ui.job.callback.SaveJobCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.UnSaveJobCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface JobDataSource {
    fun getJobs(): LiveData<Resource<PagedList<JobEntity>>>
    fun getSavedJobs(applicantId: String): LiveData<Resource<PagedList<SavedJobEntity>>>
    fun getSavedJobByJob(jobId: String, applicantId: String): LiveData<Resource<SavedJobEntity>>
    fun getJobById(jobId: String): LiveData<Resource<JobEntity>>
    fun getJobsByRecruiter(recruiterId: String): LiveData<Resource<PagedList<JobEntity>>>
    fun getJobDetails(jobId: String): LiveData<Resource<JobDetailsEntity>>
    fun saveJob(savedJob: SavedJobResponseEntity, callback: SaveJobCallback)
    fun unSaveJob(id: String, callback: UnSaveJobCallback)
    fun searchJob(searchText: String): LiveData<Resource<PagedList<JobEntity>>>
    fun getFilteredJobs(searchText: String): LiveData<PagedList<JobEntity>>
}