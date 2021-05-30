package org.d3ifcool.dissajobapplicant.data.source.local.entity.media

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "media")
data class MediaEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "media_name")
    var mediaName: String? = "-",

    @ColumnInfo(name = "media_description")
    var mediaDescription: String? = "-",

    @ColumnInfo(name = "applicant_id")
    var applicantId: String,

    @ColumnInfo(name = "file_id")
    var fileId: String
) : Parcelable