package org.d3ifcool.dissajobapplicant.data.source.local.entity.application

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @NonNull
    @ColumnInfo(name = "applicant_id")
    var applicantId: String? = "-",

    @NonNull
    @ColumnInfo(name = "job_id")
    var jobId: String? = "-",

    @ColumnInfo(name = "apply_date")
    var applyDate: String? = "-",

    @ColumnInfo(name = "updated_date")
    var updatedDate: String? = "-",

    @ColumnInfo(name = "status")
    var status: String? = "-",

    @ColumnInfo(name = "is_marked")
    var isMarked: Boolean? = false
)