package org.d3ifcool.dissajobapplicant.data.source.remote.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity
import org.d3ifcool.dissajobapplicant.ui.cv.RetrieveCvFromDatabase
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.utils.CvHelper
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.InsertToDatabaseCallback

class RemoteCvSource private constructor(
    private val cvHelper: CvHelper
) {
    companion object {
        @Volatile
        private var instance: RemoteCvSource? = null

        fun getInstance(cvHelper: CvHelper): RemoteCvSource =
            instance ?: synchronized(this) {
                instance ?: RemoteCvSource(cvHelper)
            }
    }

    fun getCvId(
        applicantId: String,
        callback: RetrieveCvFromDatabase
    ): LiveData<ApiResponse<List<CvResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultCv = MutableLiveData<ApiResponse<List<CvResponseEntity>>>()
        cvHelper.getCvId(applicantId, object : RetrieveCvFromDatabase {
            override fun onAllCvReceived(cvResponse: List<CvResponseEntity>): List<CvResponseEntity> {
                resultCv.value = ApiResponse.success(callback.onAllCvReceived(cvResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return cvResponse
            }
        })
        return resultCv
    }

    fun uploadCv(
        cv: Uri,
        callback: UploadFileCallback
    ) {
        EspressoIdlingResource.increment()
        cvHelper.uploadCv(cv, object : UploadFileCallback {
            override fun onSuccessUpload(fileId: String) {
                callback.onSuccessUpload(fileId)
                EspressoIdlingResource.decrement()
            }

            override fun onFailureUpload(messageId: Int) {
                callback.onFailureUpload(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun storeFileId(
        cvData: CvResponseEntity,
        callback: InsertToDatabaseCallback
    ) {
        EspressoIdlingResource.increment()
        cvHelper.storeFileId(cvData, object : InsertToDatabaseCallback {
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