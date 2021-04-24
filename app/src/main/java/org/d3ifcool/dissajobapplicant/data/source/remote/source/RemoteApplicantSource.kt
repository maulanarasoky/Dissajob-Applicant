package org.d3ifcool.dissajobapplicant.data.source.remote.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.ui.profile.callback.LoadApplicantDetailsCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UpdateProfileCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadProfilePictureCallback
import org.d3ifcool.dissajobapplicant.ui.resetpassword.ResetPasswordCallback
import org.d3ifcool.dissajobapplicant.ui.signin.SignInCallback
import org.d3ifcool.dissajobapplicant.ui.signup.SignUpCallback
import org.d3ifcool.dissajobapplicant.utils.ApplicantHelper
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource

class RemoteApplicantSource private constructor(
    private val applicantHelper: ApplicantHelper
) {
    companion object {
        @Volatile
        private var instance: RemoteApplicantSource? = null

        fun getInstance(applicantHelper: ApplicantHelper): RemoteApplicantSource =
            instance ?: synchronized(this) {
                instance ?: RemoteApplicantSource(applicantHelper)
            }
    }

    fun signUp(
        email: String,
        password: String,
        applicant: ApplicantResponseEntity,
        callback: SignUpCallback
    ) {
        EspressoIdlingResource.increment()
        applicantHelper.signUp(email, password, applicant, object : SignUpCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun signIn(email: String, password: String, callback: SignInCallback) {
        EspressoIdlingResource.increment()
        applicantHelper.signIn(email, password, object : SignInCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onNotVerified() {
                callback.onNotVerified()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure() {
                callback.onFailure()
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getApplicantData(
        applicantId: String,
        callback: LoadApplicantDetailsCallback
    ): LiveData<ApiResponse<ApplicantResponseEntity>> {
        EspressoIdlingResource.increment()
        val resultApplicantData = MutableLiveData<ApiResponse<ApplicantResponseEntity>>()
        applicantHelper.getApplicantData(applicantId, object : LoadApplicantDetailsCallback {
            override fun onApplicantDetailsReceived(applicantResponse: ApplicantResponseEntity): ApplicantResponseEntity {
                resultApplicantData.value =
                    ApiResponse.success(callback.onApplicantDetailsReceived(applicantResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return applicantResponse
            }
        })
        return resultApplicantData
    }

    fun updateApplicantData(applicantProfile: ApplicantResponseEntity, callback: UpdateProfileCallback) {
        EspressoIdlingResource.increment()
        applicantHelper.updateApplicantData(applicantProfile, object : UpdateProfileCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun uploadApplicantProfilePicture(image: Uri, callback: UploadProfilePictureCallback) {
        EspressoIdlingResource.increment()
        applicantHelper.uploadApplicantProfilePicture(image, object : UploadProfilePictureCallback {
            override fun onSuccessUpload(imageId: String) {
                callback.onSuccessUpload(imageId)
                EspressoIdlingResource.decrement()
            }

            override fun onFailureUpload(messageId: Int) {
                callback.onFailureUpload(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun updateApplicantEmail(
        userId: String,
        newEmail: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        EspressoIdlingResource.increment()
        applicantHelper.updateApplicantEmail(userId, newEmail, password, object : UpdateProfileCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun updateApplicantPhoneNumber(
        userId: String,
        newPhoneNumber: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        EspressoIdlingResource.increment()
        applicantHelper.updateApplicantPhoneNumber(
            userId,
            newPhoneNumber,
            password,
            object : UpdateProfileCallback {
                override fun onSuccess() {
                    callback.onSuccess()
                    EspressoIdlingResource.decrement()
                }

                override fun onFailure(messageId: Int) {
                    callback.onFailure(messageId)
                    EspressoIdlingResource.decrement()
                }
            })
    }

    fun updateApplicantPassword(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) {
        EspressoIdlingResource.increment()
        applicantHelper.updateApplicantPassword(
            email,
            oldPassword,
            newPassword,
            object : UpdateProfileCallback {
                override fun onSuccess() {
                    callback.onSuccess()
                    EspressoIdlingResource.decrement()
                }

                override fun onFailure(messageId: Int) {
                    callback.onFailure(messageId)
                    EspressoIdlingResource.decrement()
                }
            })
    }

    fun resetPassword(
        email: String,
        callback: ResetPasswordCallback
    ) {
        EspressoIdlingResource.increment()
        applicantHelper.resetPassword(email, object : ResetPasswordCallback {
            override fun onSuccess() {
                callback.onSuccess()
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(messageId: Int) {
                callback.onFailure(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }
}