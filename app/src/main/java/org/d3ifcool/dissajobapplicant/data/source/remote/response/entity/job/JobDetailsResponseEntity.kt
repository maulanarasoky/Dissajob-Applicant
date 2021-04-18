package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job

import com.google.firebase.database.Exclude

data class JobDetailsResponseEntity(
    @get:Exclude
    var id: String,
    val title: String? = "-",
    val description: String? = "-",
    val qualification: String? = "-",
    val employment: String? = "-",
    val industry: String? = "-",
    val salary: Int? = 0,
    var postedBy: String? = "-",
    var postedDate: String? = "-",
    var updatedDate: String? = "-",
    var closedDate: String? = "-",
    var isOpen: Boolean? = true
)