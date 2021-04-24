package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job

import com.google.firebase.database.Exclude

data class SavedJobResponseEntity(
    @get:Exclude
    var id: String? = "-",
    val jobId: String? = "-",
)