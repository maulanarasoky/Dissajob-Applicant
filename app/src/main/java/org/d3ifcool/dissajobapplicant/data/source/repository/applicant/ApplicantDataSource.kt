package org.d3ifcool.dissajobapplicant.data.source.repository.applicant

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.ui.signin.SignInCallback
import org.d3ifcool.dissajobapplicant.ui.signup.SignUpCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface ApplicantDataSource {
    fun signUp(email: String, password: String, applicant: ApplicantResponseEntity, callback: SignUpCallback)
    fun signIn(email: String, password: String, callback: SignInCallback)
    fun getApplicantData(applicantId: String): LiveData<Resource<ApplicantEntity>>
}