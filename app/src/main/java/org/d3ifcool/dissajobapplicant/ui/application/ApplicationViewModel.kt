package org.d3ifcool.dissajobapplicant.ui.application

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.application.ApplicationRepository
import org.d3ifcool.dissajobapplicant.ui.job.callback.ApplyJobCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class ApplicationViewModel(private val applicationRepository: ApplicationRepository) : ViewModel() {
    fun getApplications(applicantId: String): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getApplications(applicantId)

    fun getApplicationById(applicationId: String): LiveData<Resource<ApplicationEntity>> =
        applicationRepository.getApplicationById(applicationId)

    fun getApplicationByJob(
        jobId: String,
        applicantId: String
    ): LiveData<Resource<ApplicationEntity>> =
        applicationRepository.getApplicationByJob(jobId, applicantId)

    fun getAcceptedApplications(applicantId: String): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getAcceptedApplications(applicantId)

    fun getRejectedApplications(applicantId: String): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getRejectedApplications(applicantId)

    fun getMarkedApplications(applicantId: String): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getMarkedApplications(applicantId)

    fun insertApplication(application: ApplicationResponseEntity, callback: ApplyJobCallback) =
        applicationRepository.insertApplication(application, callback)
}