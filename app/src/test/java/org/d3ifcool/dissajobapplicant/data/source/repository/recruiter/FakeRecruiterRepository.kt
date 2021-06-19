package org.d3ifcool.dissajobapplicant.data.source.repository.recruiter

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobapplicant.data.NetworkBoundResource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalRecruiterSource
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.recruiter.RecruiterResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteRecruiterSource
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.vo.Resource

class FakeRecruiterRepository(
    private val remoteRecruiterSource: RemoteRecruiterSource,
    private val localRecruiterSource: LocalRecruiterSource,
    private val appExecutors: AppExecutors
) : RecruiterDataSource {

    override fun getRecruiterData(recruiterId: String): LiveData<Resource<RecruiterEntity>> {
        return object :
            NetworkBoundResource<RecruiterEntity, RecruiterResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<RecruiterEntity> =
                localRecruiterSource.getRecruiterData(recruiterId)

            override fun shouldFetch(data: RecruiterEntity?): Boolean =
                data == null

            public override fun createCall(): LiveData<ApiResponse<RecruiterResponseEntity>> =
                remoteRecruiterSource.getRecruiterData(
                    recruiterId,
                    object : RemoteRecruiterSource.LoadRecruiterDataCallback {
                        override fun onRecruiterDataReceived(recruiterResponse: RecruiterResponseEntity): RecruiterResponseEntity {
                            return recruiterResponse
                        }
                    })

            public override fun saveCallResult(data: RecruiterResponseEntity) {
                val userProfile = RecruiterEntity(
                    data.id,
                    data.firstName,
                    data.lastName,
                    data.fullName,
                    data.email,
                    data.phoneNumber,
                    data.address,
                    data.aboutMe,
                    data.imagePath,
                )
                localRecruiterSource.insertRecruiterData(userProfile)
            }
        }.asLiveData()
    }
}