package org.d3ifcool.dissajobapplicant.data.source.local.entity.job

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_jobs")
data class SavedJobEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @NonNull
    @ColumnInfo(name = "job_id")
    val jobId: String,

    @NonNull
    @ColumnInfo(name = "applicant_id")
    val applicantId: String
)