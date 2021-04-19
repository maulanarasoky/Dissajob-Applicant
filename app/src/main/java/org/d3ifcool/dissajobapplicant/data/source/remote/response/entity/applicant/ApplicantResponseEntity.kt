package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant

import com.google.firebase.database.Exclude

data class ApplicantResponseEntity(
    @get:Exclude
    var id: String? = "-",
    var firstName: String? = "-",
    var lastName: String? = "-",
    var fullName: String? = "-",
    var email: String? = "-",
    var aboutMe: String? = "-",
    var phoneNumber: String? = "-",
    var imagePath: String? = "-"
)