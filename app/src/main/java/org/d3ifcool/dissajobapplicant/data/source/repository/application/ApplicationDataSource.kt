package org.d3ifcool.dissajobapplicant.data.source.repository.application

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobapplicant.ui.job.callback.ApplyJobCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface ApplicationDataSource {
    fun getApplications(): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun getApplicationById(applicationId: String): LiveData<Resource<ApplicationEntity>>
    fun getApplicationByJob(
        jobId: String,
        applicantId: String
    ): LiveData<Resource<ApplicationEntity>>

    fun getAcceptedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun getRejectedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun getMarkedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun insertApplication(application: ApplicationResponseEntity, callback: ApplyJobCallback)
}