package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.notification

import com.google.firebase.database.Exclude

data class NotificationResponseEntity(
    @get:Exclude
    var id: String,
    var jobId: String,
    var applicationId: String,
    var applicantId: String,
    var recruiterId: String,
    var notificationDate: String? = "-",
)