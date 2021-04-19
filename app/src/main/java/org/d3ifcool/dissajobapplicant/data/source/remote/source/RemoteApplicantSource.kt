package org.d3ifcool.dissajobapplicant.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.ui.applicant.callback.LoadApplicantDetailsCallback
import org.d3ifcool.dissajobapplicant.utils.ApplicantHelper
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource

class RemoteApplicantSource private constructor(
    private val applicantHelper: ApplicantHelper
) {
    companion object {
        @Volatile
        private var instance: RemoteApplicantSource? = null

        fun getInstance(applicantHelper: ApplicantHelper): RemoteApplicantSource =
            instance ?: synchronized(this) {
                instance ?: RemoteApplicantSource(applicantHelper)
            }
    }

    fun getApplicantData(
        applicantId: String,
        callback: LoadApplicantDetailsCallback
    ): LiveData<ApiResponse<ApplicantResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultApplicantData = MutableLiveData<ApiResponse<ApplicantResponseEntity>>()
        applicantHelper.getApplicantDetails(applicantId, object : LoadApplicantDetailsCallback {
            override fun onApplicantDetailsReceived(applicantResponse: ApplicantResponseEntity): ApplicantResponseEntity {
                resultApplicantData.value =
                    ApiResponse.success(callback.onApplicantDetailsReceived(applicantResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicantResponse
            }
        })
        return resultApplicantData
    }
}