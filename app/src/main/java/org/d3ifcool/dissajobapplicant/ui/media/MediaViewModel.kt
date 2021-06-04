package org.d3ifcool.dissajobapplicant.ui.media

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.media.MediaResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.media.MediaRepository
import org.d3ifcool.dissajobapplicant.ui.media.callback.AddMediaCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.DeleteMediaCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.UpdateMediaCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class MediaViewModel(private val mediaRepository: MediaRepository) : ViewModel() {
    fun getApplicantMedia(applicantId: String): LiveData<Resource<PagedList<MediaEntity>>> =
        mediaRepository.getApplicantMedia(applicantId)

    fun uploadMedia(
        media: Uri,
        callback: UploadFileCallback
    ) = mediaRepository.uploadMedia(media, callback)

    fun addMedia(
        mediaData: MediaResponseEntity,
        callback: AddMediaCallback
    ) = mediaRepository.addMedia(mediaData, callback)

    fun updateMedia(
        mediaData: MediaResponseEntity,
        callback: UpdateMediaCallback
    ) = mediaRepository.updateMedia(mediaData, callback)

    fun deleteMedia(
        id: String,
        fileId: String,
        callback: DeleteMediaCallback
    ) = mediaRepository.deleteMedia(id, fileId, callback)

    fun getMediaById(fileId: String): LiveData<ByteArray> =
        mediaRepository.getMediaById(fileId)
}