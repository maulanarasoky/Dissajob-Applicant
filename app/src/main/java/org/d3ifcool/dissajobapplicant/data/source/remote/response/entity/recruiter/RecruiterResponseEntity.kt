package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.recruiter

import com.google.firebase.database.Exclude

data class RecruiterResponseEntity(
    @get:Exclude
    var id: String? = "-",
    val firstName: String? = "-",
    val lastName: String? = "-",
    val fullName: String? = "-",
    val email: String? = "-",
    val address: String? = "-",
    val phoneNumber: String? = "-",
    val role: String? = "-",
    val imagePath: String? = "-"
)