package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.application

import com.google.firebase.database.Exclude

data class ApplicationResponseEntity(
    @get:Exclude
    var id: String? = "-",
    var applicantId: String? = "-",
    var jobId: String? = "-",
    var applyDate: String? = "-",
    var updatedDate: String? = "-",
    var status: String? = "-",
    var isMarked: Boolean? = false,
)