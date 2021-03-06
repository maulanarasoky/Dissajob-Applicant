package org.d3ifcool.dissajobapplicant.data.source.repository.job

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.NetworkBoundResource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.SavedJobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalJobSource
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.SavedJobResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobapplicant.ui.job.callback.*
import org.d3ifcool.dissajobapplicant.ui.job.savedjob.LoadSavedJobDataCallback
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.NetworkStateCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class JobRepository private constructor(
    private val remoteJobSource: RemoteJobSource, private val localJobSource: LocalJobSource,
    private val appExecutors: AppExecutors, private val networkCallback: NetworkStateCallback
) :
    JobDataSource {

    companion object {
        @Volatile
        private var instance: JobRepository? = null

        fun getInstance(
            remoteJob: RemoteJobSource,
            localJob: LocalJobSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): JobRepository =
            instance ?: synchronized(this) {
                instance ?: JobRepository(remoteJob, localJob, appExecutors, networkCallback)
            }
    }

    override fun getJobs(): LiveData<Resource<PagedList<JobEntity>>> {
        return object :
            NetworkBoundResource<PagedList<JobEntity>, List<JobResponseEntity>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<JobEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localJobSource.getJobs(), config).build()
            }

            override fun shouldFetch(data: PagedList<JobEntity>?): Boolean =
                networkCallback.hasConnectivity()

            public override fun createCall(): LiveData<ApiResponse<List<JobResponseEntity>>> =
                remoteJobSource.getJobs(object : LoadJobsCallback {
                    override fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity> {
                        return jobResponse
                    }
                })

            public override fun saveCallResult(data: List<JobResponseEntity>) {
                localJobSource.deleteAllJobs()
                val jobList = ArrayList<JobEntity>()
                for (response in data) {
                    val job = JobEntity(
                        response.id,
                        response.title,
                        response.address,
                        response.postedBy,
                        response.postedDate,
                        response.isOpen,
                        response.isOpenForDisability
                    )
                    jobList.add(job)
                }
                localJobSource.insertJobs(jobList)
            }
        }.asLiveData()
    }

    override fun getSavedJobs(applicantId: String): LiveData<Resource<PagedList<SavedJobEntity>>> {
        return object :
            NetworkBoundResource<PagedList<SavedJobEntity>, List<SavedJobResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<SavedJobEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localJobSource.getSavedJobs(applicantId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<SavedJobEntity>?): Boolean =
                networkCallback.hasConnectivity()

            public override fun createCall(): LiveData<ApiResponse<List<SavedJobResponseEntity>>> =
                remoteJobSource.getSavedJobs(applicantId, object : LoadSavedJobsCallback {
                    override fun onAllJobsReceived(jobResponse: List<SavedJobResponseEntity>): List<SavedJobResponseEntity> {
                        return jobResponse
                    }
                })

            public override fun saveCallResult(data: List<SavedJobResponseEntity>) {
                localJobSource.deleteAllSavedJobs(applicantId)
                val jobList = ArrayList<SavedJobEntity>()
                for (response in data) {
                    val job = SavedJobEntity(
                        response.id,
                        response.jobId,
                        response.applicantId
                    )
                    jobList.add(job)
                }
                localJobSource.insertSavedJobs(jobList)
            }
        }.asLiveData()
    }

    override fun getSavedJobByJob(
        jobId: String,
        applicantId: String
    ): LiveData<Resource<SavedJobEntity>> {
        return object :
            NetworkBoundResource<SavedJobEntity, SavedJobResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<SavedJobEntity> =
                localJobSource.getSavedJobByJob(jobId, applicantId)

            override fun shouldFetch(data: SavedJobEntity?): Boolean =
                networkCallback.hasConnectivity()

            public override fun createCall(): LiveData<ApiResponse<SavedJobResponseEntity>> =
                remoteJobSource.getSavedJobByJob(
                    jobId,
                    applicantId,
                    object : LoadSavedJobDataCallback {
                        override fun onSavedJobDataCallback(savedJobResponse: SavedJobResponseEntity): SavedJobResponseEntity {
                            return savedJobResponse
                        }
                    })

            public override fun saveCallResult(data: SavedJobResponseEntity) {
                localJobSource.deleteCurrentSavedJob(jobId, applicantId)
                val savedJobList = ArrayList<SavedJobEntity>()
                val savedJob = SavedJobEntity(
                    data.id,
                    data.jobId,
                    data.applicantId
                )
                savedJobList.add(savedJob)
                localJobSource.insertSavedJobs(savedJobList)
            }
        }.asLiveData()
    }

    override fun getJobById(jobId: String): LiveData<Resource<JobEntity>> {
        return object :
            NetworkBoundResource<JobEntity, JobResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<JobEntity> =
                localJobSource.getJobById(jobId)

            override fun shouldFetch(data: JobEntity?): Boolean =
                networkCallback.hasConnectivity()

            public override fun createCall(): LiveData<ApiResponse<JobResponseEntity>> =
                remoteJobSource.getJobById(jobId, object : RemoteJobSource.LoadJobDataCallback {
                    override fun onJobDataReceived(jobResponse: JobResponseEntity): JobResponseEntity {
                        return jobResponse
                    }
                })

            public override fun saveCallResult(data: JobResponseEntity) {
                val jobList = ArrayList<JobEntity>()
                val job = JobEntity(
                    data.id,
                    data.title,
                    data.address,
                    data.postedBy,
                    data.postedDate,
                    data.isOpen,
                    data.isOpenForDisability
                )
                jobList.add(job)
                localJobSource.insertJobs(jobList)
            }
        }.asLiveData()
    }

    override fun getJobsByRecruiter(recruiterId: String): LiveData<Resource<PagedList<JobEntity>>> {
        return object :
            NetworkBoundResource<PagedList<JobEntity>, List<JobResponseEntity>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<JobEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localJobSource.getJobsByRecruiter(recruiterId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<JobEntity>?): Boolean =
                networkCallback.hasConnectivity()

            public override fun createCall(): LiveData<ApiResponse<List<JobResponseEntity>>> =
                remoteJobSource.getJobsByRecruiter(recruiterId, object : LoadJobsCallback {
                    override fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity> {
                        return jobResponse
                    }
                })

            public override fun saveCallResult(data: List<JobResponseEntity>) {
                localJobSource.deleteAllJobsByRecruiter(recruiterId)
                val jobList = ArrayList<JobEntity>()
                for (response in data) {
                    val job = JobEntity(
                        response.id,
                        response.title,
                        response.address,
                        response.postedBy,
                        response.postedDate,
                        response.isOpen,
                        response.isOpenForDisability
                    )
                    jobList.add(job)
                }
                localJobSource.insertJobs(jobList)
            }
        }.asLiveData()
    }

    override fun getJobDetails(jobId: String): LiveData<Resource<JobDetailsEntity>> {
        return object :
            NetworkBoundResource<JobDetailsEntity, JobDetailsResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<JobDetailsEntity> =
                localJobSource.getJobDetails(jobId)

            override fun shouldFetch(data: JobDetailsEntity?): Boolean =
                networkCallback.hasConnectivity()

            public override fun createCall(): LiveData<ApiResponse<JobDetailsResponseEntity>> =
                remoteJobSource.getJobDetails(
                    jobId,
                    object : LoadJobDetailsCallback {
                        override fun onJobDetailsReceived(jobResponse: JobDetailsResponseEntity): JobDetailsResponseEntity {
                            return jobResponse
                        }
                    })

            public override fun saveCallResult(data: JobDetailsResponseEntity) {
                val jobDetails = JobDetailsEntity(
                    data.id,
                    data.title,
                    data.description,
                    data.address,
                    data.qualification,
                    data.employment,
                    data.type,
                    data.industry,
                    data.salary,
                    data.postedBy,
                    data.postedDate,
                    data.updatedDate,
                    data.closedDate,
                    data.isOpen,
                    data.isOpenForDisability,
                    data.additionalInformation
                )
                localJobSource.insertJobDetails(jobDetails)
            }
        }.asLiveData()
    }

    override fun saveJob(savedJob: SavedJobResponseEntity, callback: SaveJobCallback) =
        appExecutors.diskIO()
            .execute { remoteJobSource.saveJob(savedJob, callback) }

    override fun unSaveJob(id: String, callback: UnSaveJobCallback) =
        appExecutors.diskIO()
            .execute {
                localJobSource.unSaveJob(id)
                remoteJobSource.unSaveJob(id, callback)
            }

    override fun searchJob(searchText: String): LiveData<Resource<PagedList<JobEntity>>> {
        return object :
            NetworkBoundResource<PagedList<JobEntity>, List<JobResponseEntity>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<JobEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localJobSource.searchJob(searchText), config).build()
            }

            override fun shouldFetch(data: PagedList<JobEntity>?): Boolean =
                networkCallback.hasConnectivity()

            public override fun createCall(): LiveData<ApiResponse<List<JobResponseEntity>>> =
                remoteJobSource.searchJob(searchText, object : LoadJobsCallback {
                    override fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity> {
                        return jobResponse
                    }
                })

            public override fun saveCallResult(data: List<JobResponseEntity>) {
                val jobList = ArrayList<JobEntity>()
                for (response in data) {
                    val job = JobEntity(
                        response.id,
                        response.title,
                        response.address,
                        response.postedBy,
                        response.postedDate,
                        response.isOpen,
                        response.isOpenForDisability
                    )
                    jobList.add(job)
                }
                localJobSource.insertJobs(jobList)
            }
        }.asLiveData()
    }

    override fun getFilteredJobs(searchText: String): LiveData<PagedList<JobEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localJobSource.getFilteredJobs(searchText), config).build()
    }
}