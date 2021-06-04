package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job

import com.google.firebase.database.Exclude

data class JobResponseEntity(
    @get:Exclude
    var id: String,
    var title: String? = "-",
    var address: String? = "-",
    var postedBy: String,
    var postedDate: String? = "-",
    var isOpen: Boolean,
    var isOpenForDisability: Boolean
)