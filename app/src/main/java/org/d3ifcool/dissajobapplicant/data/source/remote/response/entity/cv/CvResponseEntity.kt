package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv

import com.google.firebase.database.Exclude

data class CvResponseEntity(
    @get:Exclude
    var id: String? = "-",
    var cvName: String? = "-",
    var cvDescription: String? = "-",
    var applicantId: String,
    var fileId: String
)