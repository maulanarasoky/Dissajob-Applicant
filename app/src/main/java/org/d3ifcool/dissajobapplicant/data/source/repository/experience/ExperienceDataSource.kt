package org.d3ifcool.dissajobapplicant.data.source.repository.experience

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.experience.ExperienceResponseEntity
import org.d3ifcool.dissajobapplicant.ui.experience.callback.AddExperienceCallback
import org.d3ifcool.dissajobapplicant.ui.experience.callback.DeleteExperienceCallback
import org.d3ifcool.dissajobapplicant.ui.experience.callback.UpdateExperienceCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface ExperienceDataSource {
    fun getApplicantExperiences(applicantId: String): LiveData<Resource<PagedList<ExperienceEntity>>>
    fun addApplicantExperience(
        experience: ExperienceResponseEntity,
        callback: AddExperienceCallback
    )

    fun updateApplicantExperience(
        experience: ExperienceResponseEntity,
        callback: UpdateExperienceCallback
    )

    fun deleteApplicantExperience(id: String, callback: DeleteExperienceCallback)
}