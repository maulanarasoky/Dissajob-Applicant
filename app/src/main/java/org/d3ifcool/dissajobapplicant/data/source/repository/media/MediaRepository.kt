package org.d3ifcool.dissajobapplicant.data.source.repository.media

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.NetworkBoundResource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalMediaSource
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.media.MediaResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteMediaSource
import org.d3ifcool.dissajobapplicant.ui.media.callback.AddMediaCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.LoadMediaDataCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.LoadMediaFileCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.UpdateMediaCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.NetworkStateCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class MediaRepository private constructor(
    private val remoteMediaSource: RemoteMediaSource,
    private val localMediaSource: LocalMediaSource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) :
    MediaDataSource {

    companion object {
        @Volatile
        private var instance: MediaRepository? = null

        fun getInstance(
            remoteMedia: RemoteMediaSource,
            localMedia: LocalMediaSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): MediaRepository =
            instance ?: synchronized(this) {
                instance ?: MediaRepository(
                    remoteMedia,
                    localMedia,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun getApplicantMedia(applicantId: String): LiveData<Resource<PagedList<MediaEntity>>> {
        return object :
            NetworkBoundResource<PagedList<MediaEntity>, List<MediaResponseEntity>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<MediaEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localMediaSource.getApplicantMedia(applicantId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<MediaEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()
//                data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<MediaResponseEntity>>> =
                remoteMediaSource.getMediaId(applicantId, object : LoadMediaDataCallback {
                    override fun onAllMediaReceived(mediaResponse: List<MediaResponseEntity>): List<MediaResponseEntity> {
                        return mediaResponse
                    }
                })

            public override fun saveCallResult(data: List<MediaResponseEntity>) {
                val mediaList = ArrayList<MediaEntity>()
                for (response in data) {
                    val media = MediaEntity(
                        response.id,
                        response.mediaName,
                        response.mediaDescription,
                        response.applicantId,
                        response.fileId
                    )
                    mediaList.add(media)
                }

                localMediaSource.insertMedia(mediaList)
            }
        }.asLiveData()
    }

    override fun uploadMedia(media: Uri, callback: UploadFileCallback) =
        appExecutors.diskIO()
            .execute {
                remoteMediaSource.uploadMedia(
                    media,
                    callback
                )
            }

    override fun addMedia(mediaData: MediaResponseEntity, callback: AddMediaCallback) =
        appExecutors.diskIO()
            .execute {
                remoteMediaSource.addMedia(
                    mediaData,
                    callback
                )
            }

    override fun updateMedia(mediaData: MediaResponseEntity, callback: UpdateMediaCallback) =
        appExecutors.diskIO()
            .execute {
                localMediaSource.updateMedia(
                    mediaData.id,
                    mediaData.mediaName.toString(),
                    mediaData.mediaDescription.toString()
                )
                remoteMediaSource.updateMedia(
                    mediaData,
                    callback
                )
            }

    override fun getMediaById(fileId: String): LiveData<ByteArray> =
        remoteMediaSource.getMediaById(fileId, object : LoadMediaFileCallback {
            override fun onMediaFileReceived(mediaFile: ByteArray): ByteArray {
                return mediaFile
            }
        })
}