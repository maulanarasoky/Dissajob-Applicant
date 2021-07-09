package org.d3ifcool.dissajobapplicant.data.source.local.entity.notification

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "job_id")
    var jobId: String,

    @ColumnInfo(name = "application_id")
    var applicationId: String,

    @ColumnInfo(name = "applicant_id")
    var applicantId: String,

    @ColumnInfo(name = "recruiter_id")
    var recruiterId: String,

    @ColumnInfo(name = "notification_date")
    var notificationDate: String? = "-",
)