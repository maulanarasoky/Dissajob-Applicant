package org.d3ifcool.dissajobapplicant.data.source.repository.applicant

import android.net.Uri
import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobapplicant.data.NetworkBoundResource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalApplicantSource
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteApplicantSource
import org.d3ifcool.dissajobapplicant.ui.profile.callback.LoadApplicantDetailsCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UpdateProfileCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.ui.resetpassword.ResetPasswordCallback
import org.d3ifcool.dissajobapplicant.ui.signin.SignInCallback
import org.d3ifcool.dissajobapplicant.ui.signup.SignUpCallback
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.NetworkStateCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class ApplicantRepository private constructor(
    private val remoteApplicantSource: RemoteApplicantSource,
    private val localApplicantSource: LocalApplicantSource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) :
    ApplicantDataSource {

    companion object {
        @Volatile
        private var instance: ApplicantRepository? = null

        fun getInstance(
            remoteApplicant: RemoteApplicantSource,
            localApplicant: LocalApplicantSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): ApplicantRepository =
            instance ?: synchronized(this) {
                instance ?: ApplicantRepository(
                    remoteApplicant,
                    localApplicant,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun signUp(
        email: String,
        password: String,
        applicant: ApplicantResponseEntity,
        callback: SignUpCallback
    ) = appExecutors.diskIO()
        .execute { remoteApplicantSource.signUp(email, password, applicant, callback) }

    override fun signIn(email: String, password: String, callback: SignInCallback) =
        appExecutors.diskIO()
            .execute { remoteApplicantSource.signIn(email, password, callback) }

    override fun getApplicantData(applicantId: String): LiveData<Resource<ApplicantEntity>> {
        return object :
            NetworkBoundResource<ApplicantEntity, ApplicantResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<ApplicantEntity> =
                localApplicantSource.getApplicantData(applicantId)

            override fun shouldFetch(data: ApplicantEntity?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<ApplicantResponseEntity>> =
                remoteApplicantSource.getApplicantData(
                    applicantId,
                    object : LoadApplicantDetailsCallback {
                        override fun onApplicantDetailsReceived(applicantResponse: ApplicantResponseEntity): ApplicantResponseEntity {
                            return applicantResponse
                        }
                    })

            public override fun saveCallResult(data: ApplicantResponseEntity) {
                val applicant = ApplicantEntity(
                    data.id,
                    data.firstName,
                    data.lastName,
                    data.fullName,
                    data.email,
                    data.aboutMe,
                    data.phoneNumber,
                    data.imagePath
                )
                localApplicantSource.insertApplicantData(applicant)
            }
        }.asLiveData()
    }

    override fun uploadApplicantProfilePicture(image: Uri, callback: UploadFileCallback) =
        appExecutors.diskIO()
            .execute { remoteApplicantSource.uploadApplicantProfilePicture(image, callback) }

    override fun updateApplicantData(
        applicantProfile: ApplicantResponseEntity,
        callback: UpdateProfileCallback
    ) = appExecutors.diskIO()
        .execute { remoteApplicantSource.updateApplicantData(applicantProfile, callback) }

    override fun updateApplicantEmail(
        recruiterId: String,
        newEmail: String,
        password: String,
        callback: UpdateProfileCallback
    ) = appExecutors.diskIO()
        .execute {
            remoteApplicantSource.updateApplicantEmail(
                recruiterId,
                newEmail,
                password,
                callback
            )
        }

    override fun updateApplicantPhoneNumber(
        recruiterId: String,
        newPhoneNumber: String,
        password: String,
        callback: UpdateProfileCallback
    ) = appExecutors.diskIO()
        .execute {
            remoteApplicantSource.updateApplicantPhoneNumber(
                recruiterId,
                newPhoneNumber,
                password,
                callback
            )
        }

    override fun updateApplicantPassword(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) = appExecutors.diskIO()
        .execute {
            remoteApplicantSource.updateApplicantPassword(
                email,
                oldPassword,
                newPassword,
                callback
            )
        }

    override fun resetPassword(email: String, callback: ResetPasswordCallback) =
        appExecutors.diskIO()
            .execute {
                remoteApplicantSource.resetPassword(
                    email,
                    callback
                )
            }
}