package org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.history

import com.google.firebase.database.Exclude

data class SearchHistoryResponseEntity(
    @get:Exclude
    var id: String,
    var searchText: String? = "-",
    var searchDate: String? = "-",
    var applicantId: String
)