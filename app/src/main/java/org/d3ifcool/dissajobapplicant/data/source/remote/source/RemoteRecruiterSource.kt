package org.d3ifcool.dissajobapplicant.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.recruiter.RecruiterResponseEntity
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.database.RecruiterHelper

class RemoteRecruiterSource private constructor(
    private val recruiterHelper: RecruiterHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteRecruiterSource? = null

        fun getInstance(userHelper: RecruiterHelper): RemoteRecruiterSource =
            instance ?: synchronized(this) {
                instance ?: RemoteRecruiterSource(userHelper)
            }
    }

    fun getRecruiterData(
        recruiterId: String,
        callback: LoadRecruiterDataCallback
    ): LiveData<ApiResponse<RecruiterResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultUser = MutableLiveData<ApiResponse<RecruiterResponseEntity>>()
        recruiterHelper.getRecruiterData(recruiterId, object : LoadRecruiterDataCallback {
            override fun onRecruiterDataReceived(recruiterResponse: RecruiterResponseEntity): RecruiterResponseEntity {
                resultUser.value = ApiResponse.success(callback.onRecruiterDataReceived(recruiterResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return recruiterResponse
            }
        })
        return resultUser
    }

    interface LoadRecruiterDataCallback {
        fun onRecruiterDataReceived(recruiterResponse: RecruiterResponseEntity): RecruiterResponseEntity
    }
}