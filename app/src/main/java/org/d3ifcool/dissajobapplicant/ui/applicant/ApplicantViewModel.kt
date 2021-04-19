package org.d3ifcool.dissajobapplicant.ui.applicant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.applicant.ApplicantRepository
import org.d3ifcool.dissajobapplicant.vo.Resource

class ApplicantViewModel(private val applicantRepository: ApplicantRepository) : ViewModel() {
    fun getApplicantDetails(applicantId: String): LiveData<Resource<ApplicantEntity>> =
        applicantRepository.getApplicantData(applicantId)
}