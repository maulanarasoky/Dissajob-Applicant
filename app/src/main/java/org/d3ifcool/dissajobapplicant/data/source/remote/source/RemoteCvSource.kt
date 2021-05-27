package org.d3ifcool.dissajobapplicant.data.source.remote.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity
import org.d3ifcool.dissajobapplicant.ui.cv.callback.AddCvCallback
import org.d3ifcool.dissajobapplicant.ui.cv.callback.LoadCvCallback
import org.d3ifcool.dissajobapplicant.ui.cv.callback.LoadCvFileCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.database.CvHelper
import java.io.InputStream

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
        callback: LoadCvCallback
    ): LiveData<ApiResponse<List<CvResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultCv = MutableLiveData<ApiResponse<List<CvResponseEntity>>>()
        cvHelper.getCvId(applicantId, object : LoadCvCallback {
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

    fun addCv(
        cvData: CvResponseEntity,
        callback: AddCvCallback
    ) {
        EspressoIdlingResource.increment()
        cvHelper.addCv(cvData, object : AddCvCallback {
            override fun onSuccessAdding() {
                callback.onSuccessAdding()
                EspressoIdlingResource.decrement()
            }

            override fun onFailureAdding(messageId: Int) {
                callback.onFailureAdding(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getCvById(
        fileId: String,
        callback: LoadCvFileCallback
    ) {
        EspressoIdlingResource.increment()
        cvHelper.getCvById(fileId, object : LoadCvFileCallback {
            override fun onCvFileReceived(cvFile: ByteArray) {
                callback.onCvFileReceived(cvFile)
                EspressoIdlingResource.decrement()
            }
        })
    }
}