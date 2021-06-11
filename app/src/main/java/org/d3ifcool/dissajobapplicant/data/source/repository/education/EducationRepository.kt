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
import org.d3ifcool.dissajobapplicant.ui.education.callback.AddEducationCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.DeleteEducationCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.LoadEducationsCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.UpdateEducationCallback
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
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
                        response.id,
                        response.schoolName,
                        response.educationLevel,
                        response.fieldOfStudy,
                        response.startMonth,
                        response.startYear,
                        response.endMonth,
                        response.endYear,
                        response.description,
                        response.applicantId
                    )
                    educationList.add(education)
                }
                localEducationSource.deleteAllApplicantEducations(applicantId)
                localEducationSource.insertApplicantEducations(educationList)
            }
        }.asLiveData()
    }

    override fun addApplicantEducation(
        education: EducationResponseEntity,
        callback: AddEducationCallback
    ) =
        appExecutors.diskIO()
            .execute { remoteEducationSource.addApplicantEducation(education, callback) }

    override fun updateApplicantEducation(
        education: EducationResponseEntity,
        callback: UpdateEducationCallback
    ) = appExecutors.diskIO()
        .execute {
            remoteEducationSource.updateApplicantEducation(
                education,
                callback
            )
        }

    override fun deleteApplicantEducation(id: String, callback: DeleteEducationCallback) =
        appExecutors.diskIO()
            .execute {
                localEducationSource.deleteEducationById(id)
                remoteEducationSource.deleteApplicantEducation(id, callback)
            }
}