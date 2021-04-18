package org.d3ifcool.dissajobapplicant.data.source.repository.application

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.vo.Resource

interface ApplicationDataSource {
    fun getApplications(): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun getAcceptedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun getRejectedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>>
    fun getMarkedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>>
}