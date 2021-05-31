package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education

import com.google.firebase.database.Exclude

data class EducationResponseEntity(
    @get:Exclude
    var id: String,
    var schoolName: String? = "-",
    var educationLevel: String? = "-",
    var fieldOfStudy: String? = "-",
    var startMonth: Int,
    var startYear: Int,
    var endMonth: Int,
    var endYear: Int,
    var description: String? = "-",
    var applicantId: String
)