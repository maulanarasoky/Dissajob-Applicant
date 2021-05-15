package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.history

import com.google.firebase.database.Exclude

data class SearchHistoryResponseEntity(
    @get:Exclude
    var id: String? = "-",
    val searchText: String? = "-",
    val searchDate: String? = "-",
    val applicantId: String? = "-"
)