package org.d3ifcool.dissajobapplicant.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.SavedJobResponseEntity
import org.d3ifcool.dissajobapplicant.ui.job.callback.*
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.JobHelper

class RemoteJobSource private constructor(
    private val jobHelper: JobHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteJobSource? = null

        fun getInstance(jobHelper: JobHelper): RemoteJobSource =
            instance ?: synchronized(this) {
                instance ?: RemoteJobSource(jobHelper)
            }
    }

    fun getJobs(callback: LoadJobsCallback): LiveData<ApiResponse<List<JobResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultJob = MutableLiveData<ApiResponse<List<JobResponseEntity>>>()
        jobHelper.getJobs(object : LoadJobsCallback {
            override fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity> {
                resultJob.value = ApiResponse.success(callback.onAllJobsReceived(jobResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return jobResponse
            }
        })
        return resultJob
    }

    fun getSavedJobs(callback: LoadSavedJobsCallback): LiveData<ApiResponse<List<SavedJobResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultJob = MutableLiveData<ApiResponse<List<SavedJobResponseEntity>>>()
        jobHelper.getSavedJobs(object : LoadSavedJobsCallback {
            override fun onAllJobsReceived(jobResponse: List<SavedJobResponseEntity>): List<SavedJobResponseEntity> {
                resultJob.value = ApiResponse.success(callback.onAllJobsReceived(jobResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return jobResponse
            }
        })
        return resultJob
    }

    fun getJobById(jobId: String, callback: LoadJobDataCallback): LiveData<ApiResponse<JobResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultJob = MutableLiveData<ApiResponse<JobResponseEntity>>()
        jobHelper.getJobById(jobId, object : LoadJobDataCallback {
            override fun onJobDataReceived(jobResponse: JobResponseEntity): JobResponseEntity {
                resultJob.value = ApiResponse.success(callback.onJobDataReceived(jobResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return jobResponse
            }

        })
        return resultJob
    }

    fun getJobDetails(
        jobId: String,
        callback: LoadJobDetailsCallback
    ): LiveData<ApiResponse<JobDetailsResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultJob = MutableLiveData<ApiResponse<JobDetailsResponseEntity>>()
        jobHelper.getJobDetails(jobId, object : LoadJobDetailsCallback {
            override fun onJobDetailsReceived(jobResponse: JobDetailsResponseEntity): JobDetailsResponseEntity {
                resultJob.value = ApiResponse.success(callback.onJobDetailsReceived(jobResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return jobResponse
            }
        })
        return resultJob
    }

    fun saveJob(
        savedJob: SavedJobResponseEntity,
        callback: SaveJobCallback
    ) {
        EspressoIdlingResource.increment()
        jobHelper.saveJob(savedJob, object : SaveJobCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    interface LoadJobDataCallback {
        fun onJobDataReceived(jobResponse: JobResponseEntity): JobResponseEntity
    }
}