package org.d3ifcool.dissajobapplicant.data.source.local.entity.cv

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cv")
data class CvEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "applicant_id")
    var applicantId: String,

    @ColumnInfo(name = "file_id")
    var fileId: String,
)