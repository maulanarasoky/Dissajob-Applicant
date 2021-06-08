package org.d3ifcool.dissajobapplicant.data.source.repository.application

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.NetworkBoundResource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalApplicationSource
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteApplicationSource
import org.d3ifcool.dissajobapplicant.ui.application.callback.LoadAllApplicationsCallback
import org.d3ifcool.dissajobapplicant.ui.application.callback.LoadApplicationDataCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.ApplyJobCallback
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.NetworkStateCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class ApplicationRepository private constructor(
    private val remoteApplicationSource: RemoteApplicationSource,
    private val localApplicationSource: LocalApplicationSource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) :
    ApplicationDataSource {

    companion object {
        @Volatile
        private var instance: ApplicationRepository? = null

        fun getInstance(
            remoteApplication: RemoteApplicationSource,
            localApplication: LocalApplicationSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): ApplicationRepository =
            instance ?: synchronized(this) {
                instance ?: ApplicationRepository(
                    remoteApplication,
                    localApplication,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun getApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ApplicationEntity>, List<ApplicationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ApplicationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localApplicationSource.getApplications(),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<ApplicationEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<List<ApplicationResponseEntity>>> =
                remoteApplicationSource.getApplications(object : LoadAllApplicationsCallback {
                    override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                        return applicationsResponse
                    }
                })

            public override fun saveCallResult(data: List<ApplicationResponseEntity>) {
                val applicationList = ArrayList<ApplicationEntity>()
                for (response in data) {
                    val application = ApplicationEntity(
                        response.id,
                        response.applicantId,
                        response.jobId,
                        response.applyDate,
                        response.updatedDate,
                        response.status,
                        response.isMarked
                    )
                    applicationList.add(application)
                }

                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun getApplicationById(applicationId: String): LiveData<Resource<ApplicationEntity>> {
        return object :
            NetworkBoundResource<ApplicationEntity, ApplicationResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<ApplicationEntity> =
                localApplicationSource.getApplicationById(applicationId)

            override fun shouldFetch(data: ApplicationEntity?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<ApplicationResponseEntity>> =
                remoteApplicationSource.getApplicationById(
                    applicationId,
                    object : LoadApplicationDataCallback {
                        override fun onApplicationDataReceived(applicationResponse: ApplicationResponseEntity): ApplicationResponseEntity {
                            return applicationResponse
                        }
                    })

            public override fun saveCallResult(data: ApplicationResponseEntity) {
                val applicationList = ArrayList<ApplicationEntity>()
                val application = ApplicationEntity(
                    data.id,
                    data.applicantId,
                    data.jobId,
                    data.applyDate,
                    data.updatedDate,
                    data.status,
                    data.isMarked
                )
                applicationList.add(application)
                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun getApplicationByJob(
        jobId: String,
        applicantId: String
    ): LiveData<Resource<ApplicationEntity>> {
        return object :
            NetworkBoundResource<ApplicationEntity, ApplicationResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<ApplicationEntity> =
                localApplicationSource.getApplicationByJob(jobId, applicantId)

            override fun shouldFetch(data: ApplicationEntity?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<ApplicationResponseEntity>> =
                remoteApplicationSource.getApplicationByJob(
                    jobId,
                    applicantId,
                    object : LoadApplicationDataCallback {
                        override fun onApplicationDataReceived(applicationResponse: ApplicationResponseEntity): ApplicationResponseEntity {
                            return applicationResponse
                        }
                    })

            public override fun saveCallResult(data: ApplicationResponseEntity) {
                val applicationList = ArrayList<ApplicationEntity>()
                val application = ApplicationEntity(
                    data.id,
                    data.applicantId,
                    data.jobId,
                    data.applyDate,
                    data.updatedDate,
                    data.status,
                    data.isMarked
                )
                applicationList.add(application)
                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun getAcceptedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ApplicationEntity>, List<ApplicationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ApplicationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localApplicationSource.getAcceptedApplications(),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<ApplicationEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<List<ApplicationResponseEntity>>> =
                remoteApplicationSource.getAcceptedApplications(object :
                    LoadAllApplicationsCallback {
                    override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                        return applicationsResponse
                    }
                })

            public override fun saveCallResult(data: List<ApplicationResponseEntity>) {
                val applicationList = ArrayList<ApplicationEntity>()
                for (response in data) {
                    val application = ApplicationEntity(
                        response.id,
                        response.applicantId,
                        response.jobId,
                        response.applyDate,
                        response.updatedDate,
                        response.status,
                        response.isMarked
                    )
                    applicationList.add(application)
                }

                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun getRejectedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ApplicationEntity>, List<ApplicationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ApplicationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localApplicationSource.getRejectedApplications(),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<ApplicationEntity>?): Boolean =
                networkCallback.hasConnectivity() && localApplicationSource.getRejectedApplications() != createCall()

            public override fun createCall(): LiveData<ApiResponse<List<ApplicationResponseEntity>>> =
                remoteApplicationSource.getRejectedApplications(object :
                    LoadAllApplicationsCallback {
                    override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                        return applicationsResponse
                    }
                })

            public override fun saveCallResult(data: List<ApplicationResponseEntity>) {
                val applicationList = ArrayList<ApplicationEntity>()
                for (response in data) {
                    val application = ApplicationEntity(
                        response.id,
                        response.applicantId,
                        response.jobId,
                        response.applyDate,
                        response.updatedDate,
                        response.status,
                        response.isMarked
                    )
                    applicationList.add(application)
                }

                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun getMarkedApplications(): LiveData<Resource<PagedList<ApplicationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ApplicationEntity>, List<ApplicationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<ApplicationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localApplicationSource.getMarkedApplications(),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<ApplicationEntity>?): Boolean =
                networkCallback.hasConnectivity() && localApplicationSource.getMarkedApplications() != createCall()

            public override fun createCall(): LiveData<ApiResponse<List<ApplicationResponseEntity>>> =
                remoteApplicationSource.getMarkedApplications(object : LoadAllApplicationsCallback {
                    override fun onAllApplicationsReceived(applicationsResponse: List<ApplicationResponseEntity>): List<ApplicationResponseEntity> {
                        return applicationsResponse
                    }
                })

            public override fun saveCallResult(data: List<ApplicationResponseEntity>) {
                val applicationList = ArrayList<ApplicationEntity>()
                for (response in data) {
                    val application = ApplicationEntity(
                        response.id,
                        response.applicantId,
                        response.jobId,
                        response.applyDate,
                        response.updatedDate,
                        response.status,
                        response.isMarked
                    )
                    applicationList.add(application)
                }

                localApplicationSource.insertApplication(applicationList)
            }
        }.asLiveData()
    }

    override fun insertApplication(
        application: ApplicationResponseEntity,
        callback: ApplyJobCallback
    ) = appExecutors.diskIO()
        .execute { remoteApplicationSource.insertApplication(application, callback) }
}