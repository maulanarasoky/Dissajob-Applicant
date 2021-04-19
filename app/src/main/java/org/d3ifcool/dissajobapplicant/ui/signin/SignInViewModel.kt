package org.d3ifcool.dissajobapplicant.ui.signin

import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobapplicant.data.source.repository.applicant.ApplicantRepository

class SignInViewModel(private val applicantRepository: ApplicantRepository) : ViewModel() {
    fun signIn(
        email: String,
        password: String,
        callback: SignInCallback
    ) = applicantRepository.signIn(email, password, callback)
}