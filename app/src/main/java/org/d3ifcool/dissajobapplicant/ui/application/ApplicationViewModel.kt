package org.d3ifcool.dissajobapplicant.ui.application

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.application.ApplicationRepository
import org.d3ifcool.dissajobapplicant.vo.Resource

class ApplicationViewModel(private val applicationRepository: ApplicationRepository) : ViewModel() {
    fun getApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getApplications()

    fun getAcceptedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getAcceptedApplications()

    fun getRejectedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getRejectedApplications()

    fun getMarkedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> =
        applicationRepository.getMarkedApplications()
}