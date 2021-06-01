package org.d3ifcool.dissajobapplicant.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.experience.ExperienceResponseEntity
import org.d3ifcool.dissajobapplicant.ui.experience.AddExperienceCallback
import org.d3ifcool.dissajobapplicant.ui.experience.LoadExperiencesCallback
import org.d3ifcool.dissajobapplicant.ui.experience.UpdateExperienceCallback
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.database.ExperienceHelper

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
        callback: AddExperienceCallback
    ) {
        EspressoIdlingResource.increment()
        experienceHelper.addApplicantExperience(experience, object : AddExperienceCallback {
            override fun onSuccessAdding() {
                callback.onSuccessAdding()
                EspressoIdlingResource.decrement()
            }

            override fun onFailureAdding(messageId: Int) {
                callback.onFailureAdding(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun updateApplicantExperience(
        experience: ExperienceResponseEntity,
        callback: UpdateExperienceCallback
    ) {
        EspressoIdlingResource.increment()
        experienceHelper.updateApplicantExperience(experience, object : UpdateExperienceCallback {
            override fun onSuccessUpdate() {
                callback.onSuccessUpdate()
                EspressoIdlingResource.decrement()
            }

            override fun onFailureUpdate(messageId: Int) {
                callback.onFailureUpdate(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }
}