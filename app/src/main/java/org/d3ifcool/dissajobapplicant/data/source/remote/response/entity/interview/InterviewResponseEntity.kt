package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.interview

import com.google.firebase.database.Exclude

data class InterviewResponseEntity(
    @get:Exclude
    var id: String? = "-",
    var applicantId: String? = "-",
    var jobId: String? = "-",
    var firstAnswer: String? = "-",
    var secondAnswer: String? = "-",
    var thirdAnswer: String? = "-",
)