package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.media

import com.google.firebase.database.Exclude

data class MediaResponseEntity(
    @get:Exclude
    var id: String,
    var mediaName: String? = "-",
    var mediaDescription: String? = "-",
    var applicantId: String,
    var fileId: String
)