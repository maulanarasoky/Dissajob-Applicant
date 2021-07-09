package org.d3ifcool.dissajobapplicant.data.source.local.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.room.ApplicationDao

class LocalApplicationSource private constructor(
    private val mApplicationDao: ApplicationDao
) {

    companion object {
        private var INSTANCE: LocalApplicationSource? = null

        fun getInstance(applicationDao: ApplicationDao): LocalApplicationSource =
            INSTANCE ?: LocalApplicationSource(applicationDao)
    }

    fun getApplications(applicantId: String): DataSource.Factory<Int, ApplicationEntity> =
        mApplicationDao.getApplications(applicantId)

    fun getApplicationById(applicationId: String): LiveData<ApplicationEntity> =
        mApplicationDao.getApplicationById(applicationId)

    fun getApplicationByJob(jobId: String, applicantId: String): LiveData<ApplicationEntity> =
        mApplicationDao.getApplicationByJob(jobId, applicantId)

    fun getAcceptedApplications(applicantId: String): DataSource.Factory<Int, ApplicationEntity> =
        mApplicationDao.getApplicationsByStatus(applicantId, "accepted")

    fun getRejectedApplications(applicantId: String): DataSource.Factory<Int, ApplicationEntity> =
        mApplicationDao.getApplicationsByStatus(applicantId, "rejected")

    fun getMarkedApplications(applicantId: String): DataSource.Factory<Int, ApplicationEntity> =
        mApplicationDao.getMarkedApplications(applicantId)

    fun deleteApplicationByJob(jobId: String, applicantId: String) =
        mApplicationDao.deleteApplicationByJob(jobId, applicantId)

    fun deleteAllApplications(applicantId: String) =
        mApplicationDao.deleteAllApplications(applicantId)

    fun insertApplication(applications: List<ApplicationEntity>) =
        mApplicationDao.insertApplication(applications)
}