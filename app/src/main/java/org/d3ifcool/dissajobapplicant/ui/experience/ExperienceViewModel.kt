package org.d3ifcool.dissajobapplicant.ui.experience

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.experience.ExperienceResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.experience.ExperienceRepository
import org.d3ifcool.dissajobapplicant.ui.experience.callback.AddExperienceCallback
import org.d3ifcool.dissajobapplicant.ui.experience.callback.DeleteExperienceCallback
import org.d3ifcool.dissajobapplicant.ui.experience.callback.UpdateExperienceCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class ExperienceViewModel(private val experienceRepository: ExperienceRepository) : ViewModel() {
    fun getApplicantExperiences(applicantId: String): LiveData<Resource<PagedList<ExperienceEntity>>> =
        experienceRepository.getApplicantExperiences(applicantId)

    fun addApplicantExperience(
        experience: ExperienceResponseEntity,
        callback: AddExperienceCallback
    ) = experienceRepository.addApplicantExperience(experience, callback)

    fun updateApplicantExperience(
        experience: ExperienceResponseEntity,
        callback: UpdateExperienceCallback
    ) = experienceRepository.updateApplicantExperience(experience, callback)

    fun deleteApplicantExperience(
        id: String,
        callback: DeleteExperienceCallback
    ) = experienceRepository.deleteApplicantExperience(id, callback)
}