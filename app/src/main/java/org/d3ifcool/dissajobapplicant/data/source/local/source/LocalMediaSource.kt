package org.d3ifcool.dissajobapplicant.data.source.local.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.FileEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobapplicant.data.source.local.room.MediaDao

class LocalMediaSource private constructor(
    private val mMediaDao: MediaDao
) {

    companion object {
        private var INSTANCE: LocalMediaSource? = null

        fun getInstance(mediaDao: MediaDao): LocalMediaSource =
            INSTANCE ?: LocalMediaSource(mediaDao)
    }

    fun getApplicantMedia(applicantId: String): DataSource.Factory<Int, MediaEntity> =
        mMediaDao.getApplicantMedia(applicantId)

    fun updateMedia(id: String, mediaName: String, mediaDescription: String) =
        mMediaDao.updateMedia(id, mediaName, mediaDescription)

    fun insertMedia(media: List<MediaEntity>) = mMediaDao.insertMedia(media)

    fun getFileByteArray(id: String): LiveData<FileEntity> = mMediaDao.getFileByteArray(id)

    fun insertFileByteArray(file: FileEntity) = mMediaDao.insertFileByteArray(file)
}