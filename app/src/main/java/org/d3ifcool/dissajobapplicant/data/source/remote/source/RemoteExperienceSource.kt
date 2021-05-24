package org.d3ifcool.dissajobapplicant.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.experience.ExperienceResponseEntity
import org.d3ifcool.dissajobapplicant.ui.experience.LoadExperiencesCallback
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.database.ExperienceHelper
import org.d3ifcool.dissajobapplicant.utils.InsertToDatabaseCallback

class RemoteExperienceSource private constructor(
    private val experienceHelper: ExperienceHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteExperienceSource? = null

        fun getInstance(experienceHelper: ExperienceHelper): RemoteExperienceSource =
            instance ?: synchronized(this) {
                instance ?: RemoteExperienceSource(experienceHelper)
            }
    }

    fun getApplicantExperiences(
        applicantId: String,
        callback: LoadExperiencesCallback
    ): LiveData<ApiResponse<List<ExperienceResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultExperience = MutableLiveData<ApiResponse<List<ExperienceResponseEntity>>>()
        experienceHelper.getApplicantExperiences(applicantId, object : LoadExperiencesCallback {
            override fun onAllExperiencesReceived(experienceResponse: List<ExperienceResponseEntity>): List<ExperienceResponseEntity> {
                resultExperience.value =
                    ApiResponse.success(callback.onAllExperiencesReceived(experienceResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return experienceResponse
            }
        })
        return resultExperience
    }

    fun addApplicantExperience(
        experience: ExperienceResponseEntity,
        callback: InsertToDatabaseCallback
    ) {
        EspressoIdlingResource.increment()
        experienceHelper.addApplicantExperience(experience, object : InsertToDatabaseCallback {
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