package org.d3ifcool.dissajobapplicant.data.source.remote.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.media.MediaResponseEntity
import org.d3ifcool.dissajobapplicant.ui.media.callback.AddMediaCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.LoadMediaDataCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.LoadMediaFileCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.UpdateMediaCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.database.MediaHelper

class RemoteMediaSource private constructor(
    private val mediaHelper: MediaHelper
) {
    companion object {
        @Volatile
        private var instance: RemoteMediaSource? = null

        fun getInstance(mediaHelper: MediaHelper): RemoteMediaSource =
            instance ?: synchronized(this) {
                instance ?: RemoteMediaSource(mediaHelper)
            }
    }

    fun getMediaId(
        applicantId: String,
        callback: LoadMediaDataCallback
    ): LiveData<ApiResponse<List<MediaResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultMedia = MutableLiveData<ApiResponse<List<MediaResponseEntity>>>()
        mediaHelper.getMediaId(applicantId, object : LoadMediaDataCallback {
            override fun onAllMediaReceived(mediaResponse: List<MediaResponseEntity>): List<MediaResponseEntity> {
                resultMedia.value =
                    ApiResponse.success(callback.onAllMediaReceived(mediaResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return mediaResponse
            }
        })
        return resultMedia
    }

    fun uploadMedia(
        media: Uri,
        callback: UploadFileCallback
    ) {
        EspressoIdlingResource.increment()
        mediaHelper.uploadMedia(media, object : UploadFileCallback {
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

    fun addMedia(
        mediaData: MediaResponseEntity,
        callback: AddMediaCallback
    ) {
        EspressoIdlingResource.increment()
        mediaHelper.addMedia(mediaData, object : AddMediaCallback {
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

    fun updateMedia(
        mediaData: MediaResponseEntity,
        callback: UpdateMediaCallback
    ) {
        EspressoIdlingResource.increment()
        mediaHelper.updateMedia(mediaData, object : UpdateMediaCallback {
            override fun onSuccessUpdate() {
                callback.onSuccessUpdate()
                EspressoIdlingResource.decrement()
            }

            override fun onFailureUpdate(messageId: Int) {
                callback.onFailureUpdate(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getMediaById(
        fileId: String,
        callback: LoadMediaFileCallback
    ): LiveData<ByteArray> {
        EspressoIdlingResource.increment()
        val resultMedia = MutableLiveData<ByteArray>()
        mediaHelper.getMediaById(fileId, object : LoadMediaFileCallback {
            override fun onMediaFileReceived(mediaFile: ByteArray): ByteArray {
                resultMedia.value = callback.onMediaFileReceived(mediaFile)
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return mediaFile
            }
        })
        return resultMedia
    }
}