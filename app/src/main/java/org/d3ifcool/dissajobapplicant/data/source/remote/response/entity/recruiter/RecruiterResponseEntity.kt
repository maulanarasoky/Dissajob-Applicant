package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.recruiter

import com.google.firebase.database.Exclude

data class RecruiterResponseEntity(
    @get:Exclude
    var id: String,
    var firstName: String? = "-",
    var lastName: String? = "-",
    var fullName: String? = "-",
    var email: String? = "-",
    var address: String? = "-",
    var phoneNumber: String? = "-",
    var imagePath: String? = "-"
)