package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job

import com.google.firebase.database.Exclude

data class JobResponseEntity(
    @get:Exclude
    var id: String? = "-",
    val title: String? = "-",
    val address: String? = "-",
    val postedBy: String? = "-",
    val postedDate: String? = "-",
    val isOpen: Boolean? = true
)