package org.d3ifcool.dissajobapplicant.data.source.repository.applicant

import android.net.Uri
import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UpdateProfileCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadProfilePictureCallback
import org.d3ifcool.dissajobapplicant.ui.signin.SignInCallback
import org.d3ifcool.dissajobapplicant.ui.signup.SignUpCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface ApplicantDataSource {
    fun signUp(email: String, password: String, applicant: ApplicantResponseEntity, callback: SignUpCallback)
    fun signIn(email: String, password: String, callback: SignInCallback)
    fun getApplicantData(applicantId: String): LiveData<Resource<ApplicantEntity>>
    fun uploadApplicantProfilePicture(image: Uri, callback: UploadProfilePictureCallback)
    fun updateApplicantData(applicantProfile: ApplicantResponseEntity, callback: UpdateProfileCallback)
    fun updateApplicantEmail(recruiterId: String, newEmail: String, password: String, callback: UpdateProfileCallback)
    fun updateApplicantPhoneNumber(recruiterId: String, newPhoneNumber: String, password: String, callback: UpdateProfileCallback)
    fun updateApplicantPassword(email: String, oldPassword: String, newPassword: String, callback: UpdateProfileCallback)
}