package org.d3ifcool.dissajobapplicant.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobapplicant.ui.education.LoadEducationsCallback
import org.d3ifcool.dissajobapplicant.utils.EducationHelper
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.InsertToDatabaseCallback

class RemoteEducationSource private constructor(
    private val educationHelper: EducationHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteEducationSource? = null

        fun getInstance(educationHelper: EducationHelper): RemoteEducationSource =
            instance ?: synchronized(this) {
                instance ?: RemoteEducationSource(educationHelper)
            }
    }

    fun getApplicantEducations(
        applicantId: String,
        callback: LoadEducationsCallback
    ): LiveData<ApiResponse<List<EducationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultEducation = MutableLiveData<ApiResponse<List<EducationResponseEntity>>>()
        educationHelper.getApplicantEducations(applicantId, object : LoadEducationsCallback {
            override fun onAllEducationsReceived(educationResponse: List<EducationResponseEntity>): List<EducationResponseEntity> {
                resultEducation.value =
                    ApiResponse.success(callback.onAllEducationsReceived(educationResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return educationResponse
            }
        })
        return resultEducation
    }

    fun addApplicantEducation(
        education: EducationResponseEntity,
        callback: InsertToDatabaseCallback
    ) {
        EspressoIdlingResource.increment()
        educationHelper.addApplicantEducation(education, object : InsertToDatabaseCallback {
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
}