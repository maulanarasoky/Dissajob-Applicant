package org.d3ifcool.dissajobapplicant.ui.signup

import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.applicant.ApplicantRepository

class SignUpViewModel(private val applicantRepository: ApplicantRepository) : ViewModel() {
    fun signUp(
        email: String,
        password: String,
        applicant: ApplicantResponseEntity,
        callback: SignUpCallback
    ) = applicantRepository.signUp(email, password, applicant, callback)
}