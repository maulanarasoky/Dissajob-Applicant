package org.d3ifcool.dissajobapplicant.data.source.repository.applicant

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobapplicant.data.NetworkBoundResource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalApplicantSource
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteApplicantSource
import org.d3ifcool.dissajobapplicant.ui.applicant.callback.LoadApplicantDetailsCallback
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
                remoteApplicantSource.getApplicantData(applicantId, object : LoadApplicantDetailsCallback {
                    override fun onApplicantDetailsReceived(applicantResponse: ApplicantResponseEntity): ApplicantResponseEntity {
                        return applicantResponse
                    }
                })

            public override fun saveCallResult(data: ApplicantResponseEntity) {
                val applicant = ApplicantEntity(
                    data.id.toString(),
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
}