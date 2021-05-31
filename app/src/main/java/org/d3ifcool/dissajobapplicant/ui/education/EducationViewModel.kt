package org.d3ifcool.dissajobapplicant.ui.education

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.education.EducationRepository
import org.d3ifcool.dissajobapplicant.ui.education.callback.AddEducationCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.DeleteEducationCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.UpdateEducationCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class EducationViewModel(private val educationRepository: EducationRepository) : ViewModel() {
    fun getApplicantEducations(applicantId: String): LiveData<Resource<PagedList<EducationEntity>>> =
        educationRepository.getApplicantEducations(applicantId)

    fun addApplicantEducation(
        education: EducationResponseEntity,
        callback: AddEducationCallback
    ) = educationRepository.addApplicantEducation(education, callback)

    fun updateApplicantEducation(
        education: EducationResponseEntity,
        callback: UpdateEducationCallback
    ) = educationRepository.updateApplicantEducation(education, callback)

    fun deleteApplicantEducation(
        id: String,
        callback: DeleteEducationCallback
    ) = educationRepository.deleteApplicantEducation(id, callback)
}