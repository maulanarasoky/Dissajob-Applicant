package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education

import com.google.firebase.database.Exclude

data class EducationResponseEntity(
    @get:Exclude
    var id: String? = "-",
    var schoolName: String? = "-",
    var degree: String? = "-",
    var fieldOfStudy: String? = "-",
    var startMonth: String? = "-",
    var startYear: String? = "-",
    var endMonth: String? = "-",
    var endYear: String? = "-",
    var description: String? = "-",
    var applicantId: String
)