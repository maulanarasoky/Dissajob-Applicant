package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.experience

import com.google.firebase.database.Exclude

data class ExperienceResponseEntity(
    @get:Exclude
    var id: String? = "-",
    var title: String? = "-",
    var employmentType: String? = "-",
    var companyName: String? = "-",
    var location: String? = "-",
    var startMonth: String? = "-",
    var startYear: String? = "-",
    var endMonth: String? = "-",
    var endYear: String? = "-",
    var description: String? = "-",
    var isCurrentlyWorking: Boolean? = false,
    var applicantId: String
)