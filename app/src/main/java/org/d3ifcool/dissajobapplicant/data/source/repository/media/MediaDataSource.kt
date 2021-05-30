package org.d3ifcool.dissajobapplicant.data.source.repository.media

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.media.MediaResponseEntity
import org.d3ifcool.dissajobapplicant.ui.media.callback.AddMediaCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.UpdateMediaCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface MediaDataSource {
    fun getApplicantMedia(applicantId: String): LiveData<Resource<PagedList<MediaEntity>>>
    fun uploadMedia(media: Uri, callback: UploadFileCallback)
    fun addMedia(mediaData: MediaResponseEntity, callback: AddMediaCallback)
    fun updateMedia(mediaData: MediaResponseEntity, callback: UpdateMediaCallback)
    fun getMediaById(fileId: String): LiveData<ByteArray>
}