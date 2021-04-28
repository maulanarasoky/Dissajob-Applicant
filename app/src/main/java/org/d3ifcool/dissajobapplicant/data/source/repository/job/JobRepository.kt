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
import org.d3ifcool.dissajobapplicant.ui.job.callback.LoadJobDetailsCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.LoadJobsCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.LoadSavedJobsCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.SaveJobCallback
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
                networkCallback.hasConnectivity() && loadFromDB() != createCall()
//                data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<JobResponseEntity>>> =
                remoteJobSource.getJobs(object : LoadJobsCallback {
                    override fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity> {
                        return jobResponse
                    }
                })

            public override fun saveCallResult(data: List<JobResponseEntity>) {
                val jobList = ArrayList<JobEntity>()
                for (response in data) {
                    val job = JobEntity(
                        response.id.toString(),
                        response.title,
                        response.address,
                        response.postedBy,
                        response.postedDate,
                        response.isOpen
                    )
                    jobList.add(job)
                }

                localJobSource.insertJob(jobList)
            }
        }.asLiveData()
    }

    override fun getSavedJobs(): LiveData<Resource<PagedList<SavedJobEntity>>> {
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
                return LivePagedListBuilder(localJobSource.getSavedJobs(), config).build()
            }

            override fun shouldFetch(data: PagedList<SavedJobEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()
//                data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<SavedJobResponseEntity>>> =
                remoteJobSource.getSavedJobs(object : LoadSavedJobsCallback {
                    override fun onAllJobsReceived(jobResponse: List<SavedJobResponseEntity>): List<SavedJobResponseEntity> {
                        return jobResponse
                    }
                })

            public override fun saveCallResult(data: List<SavedJobResponseEntity>) {
                val jobList = ArrayList<SavedJobEntity>()
                for (response in data) {
                    val job = SavedJobEntity(
                        response.id.toString(),
                        response.jobId
                    )
                    jobList.add(job)
                }

                localJobSource.insertSavedJob(jobList)
            }
        }.asLiveData()
    }

    override fun getJobById(jobId: String): LiveData<Resource<PagedList<JobEntity>>> {
        return object :
            NetworkBoundResource<PagedList<JobEntity>, List<JobResponseEntity>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<JobEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localJobSource.getJobById(jobId), config).build()
            }

            override fun shouldFetch(data: PagedList<JobEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()
//                data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<JobResponseEntity>>> =
                remoteJobSource.getJobById(jobId, object : LoadJobsCallback {
                    override fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity> {
                        return jobResponse
                    }
                })

            public override fun saveCallResult(data: List<JobResponseEntity>) {
                val jobList = ArrayList<JobEntity>()
                for (response in data) {
                    val job = JobEntity(
                        response.id.toString(),
                        response.title,
                        response.address,
                        response.postedBy,
                        response.postedDate,
                        response.isOpen
                    )
                    jobList.add(job)
                }

                localJobSource.insertJob(jobList)
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
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

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
                    data.isOpen
                )
                localJobSource.insertJobDetails(jobDetails)
            }
        }.asLiveData()
    }

    override fun saveJob(savedJob: SavedJobResponseEntity, callback: SaveJobCallback) =
        appExecutors.diskIO()
            .execute { remoteJobSource.saveJob(savedJob, callback) }
}