package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job

import com.google.firebase.database.Exclude

data class JobDetailsResponseEntity(
    @get:Exclude
    var id: String,
    var title: String? = "-",
    var description: String? = "-",
    var address: String? = "-",
    var qualification: String? = "-",
    var employment: String? = "-",
    var type: String? = "-",
    var industry: String? = "-",
    var salary: String? = "-",
    var postedBy: String,
    var postedDate: String? = "-",
    var updatedDate: String? = "-",
    var closedDate: String? = "-",
    var isOpen: Boolean,
    var isOpenForDisability: Boolean,
    var additionalInformation: String? = "-"
)