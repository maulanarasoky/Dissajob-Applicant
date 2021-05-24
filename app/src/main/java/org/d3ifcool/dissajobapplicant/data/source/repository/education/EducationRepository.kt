package org.d3ifcool.dissajobapplicant.data.source.repository.education

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.NetworkBoundResource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalEducationSource
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteEducationSource
import org.d3ifcool.dissajobapplicant.ui.education.LoadEducationsCallback
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.InsertToDatabaseCallback
import org.d3ifcool.dissajobapplicant.utils.NetworkStateCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class EducationRepository private constructor(
    private val remoteEducationSource: RemoteEducationSource,
    private val localEducationSource: LocalEducationSource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) :
    EducationDataSource {

    companion object {
        @Volatile
        private var instance: EducationRepository? = null

        fun getInstance(
            remoteEducation: RemoteEducationSource,
            localEducation: LocalEducationSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): EducationRepository =
            instance ?: synchronized(this) {
                instance ?: EducationRepository(
                    remoteEducation,
                    localEducation,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun getApplicantEducations(applicantId: String): LiveData<Resource<PagedList<EducationEntity>>> {
        return object :
            NetworkBoundResource<PagedList<EducationEntity>, List<EducationResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<EducationEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localEducationSource.getApplicantEducations(applicantId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<EducationEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()
//                data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<EducationResponseEntity>>> =
                remoteEducationSource.getApplicantEducations(
                    applicantId,
                    object : LoadEducationsCallback {
                        override fun onAllEducationsReceived(educationResponse: List<EducationResponseEntity>): List<EducationResponseEntity> {
                            return educationResponse
                        }
                    })

            public override fun saveCallResult(data: List<EducationResponseEntity>) {
                val educationList = ArrayList<EducationEntity>()
                for (response in data) {
                    val education = EducationEntity(
                        response.id.toString(),
                        response.schoolName,
                        response.degree,
                        response.fieldOfStudy,
                        response.startDate,
                        response.endDate,
                        response.description,
                        response.applicantId
                    )
                    educationList.add(education)
                }

                localEducationSource.insertApplicantEducations(educationList)
            }
        }.asLiveData()
    }

    override fun addApplicantEducation(
        education: EducationResponseEntity,
        callback: InsertToDatabaseCallback
    ) =
        appExecutors.diskIO()
            .execute { remoteEducationSource.addApplicantEducation(education, callback) }
}