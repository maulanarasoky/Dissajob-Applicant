package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.FileEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity

@Dao
interface MediaDao {
    @Query("SELECT * FROM media WHERE applicant_id = :applicantId")
    fun getApplicantMedia(applicantId: String): DataSource.Factory<Int, MediaEntity>

    @Query("UPDATE media SET media_name = :mediaName, media_description = :mediaDescription WHERE id = :id")
    fun updateMedia(id: String, mediaName: String, mediaDescription: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedia(media: List<MediaEntity>)

    @Query("SELECT * FROM files WHERE id = :id")
    fun getFileByteArray(id: String): LiveData<FileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFileByteArray(file: FileEntity)
}