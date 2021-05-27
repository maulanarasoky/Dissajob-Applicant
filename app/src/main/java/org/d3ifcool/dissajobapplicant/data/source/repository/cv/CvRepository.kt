package org.d3ifcool.dissajobapplicant.data.source.repository.cv

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.NetworkBoundResource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.cv.CvEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalCvSource
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteCvSource
import org.d3ifcool.dissajobapplicant.ui.cv.callback.AddCvCallback
import org.d3ifcool.dissajobapplicant.ui.cv.callback.LoadCvCallback
import org.d3ifcool.dissajobapplicant.ui.cv.callback.LoadCvFileCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.NetworkStateCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class CvRepository private constructor(
    private val remoteCvSource: RemoteCvSource,
    private val localCvSource: LocalCvSource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) :
    CvDataSource {

    companion object {
        @Volatile
        private var instance: CvRepository? = null

        fun getInstance(
            remoteCv: RemoteCvSource,
            localCv: LocalCvSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): CvRepository =
            instance ?: synchronized(this) {
                instance ?: CvRepository(
                    remoteCv,
                    localCv,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun getApplicantCv(applicantId: String): LiveData<Resource<PagedList<CvEntity>>> {
        return object :
            NetworkBoundResource<PagedList<CvEntity>, List<CvResponseEntity>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<CvEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localCvSource.getApplicantCv(applicantId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<CvEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()
//                data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<CvResponseEntity>>> =
                remoteCvSource.getCvId(applicantId, object : LoadCvCallback {
                    override fun onAllCvReceived(cvResponse: List<CvResponseEntity>): List<CvResponseEntity> {
                        return cvResponse
                    }
                })

            public override fun saveCallResult(data: List<CvResponseEntity>) {
                val cvList = ArrayList<CvEntity>()
                for (response in data) {
                    val cv = CvEntity(
                        response.id.toString(),
                        response.cvName,
                        response.cvDescription,
                        response.applicantId,
                        response.fileId
                    )
                    cvList.add(cv)
                }

                localCvSource.insertCv(cvList)
            }
        }.asLiveData()
    }

    override fun uploadCv(file: Uri, callback: UploadFileCallback) =
        appExecutors.diskIO()
            .execute {
                remoteCvSource.uploadCv(
                    file,
                    callback
                )
            }

    override fun addCv(cvData: CvResponseEntity, callback: AddCvCallback) =
        appExecutors.diskIO()
            .execute {
                remoteCvSource.addCv(
                    cvData,
                    callback
                )
            }

    override fun getCvById(fileId: String, callback: LoadCvFileCallback) =
        appExecutors.diskIO().execute { remoteCvSource.getCvById(fileId, callback) }
}