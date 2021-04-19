package org.d3ifcool.dissajobapplicant.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.applicant.ApplicantRepository
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UpdateProfileCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class ApplicantViewModel(private val applicantRepository: ApplicantRepository) : ViewModel() {
    fun getApplicantDetails(applicantId: String): LiveData<Resource<ApplicantEntity>> =
        applicantRepository.getApplicantData(applicantId)

    fun updateApplicantData(
        applicantProfile: ApplicantResponseEntity,
        callback: UpdateProfileCallback
    ) = applicantRepository.updateApplicantData(applicantProfile, callback)
}