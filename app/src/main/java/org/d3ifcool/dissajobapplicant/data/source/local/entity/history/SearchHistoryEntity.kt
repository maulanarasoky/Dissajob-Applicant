package org.d3ifcool.dissajobapplicant.data.source.local.entity.history

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_histories")
data class SearchHistoryEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "search_text")
    val searchText: String? = "-",

    @ColumnInfo(name = "search_date")
    val searchDate: String? = "-",

    @ColumnInfo(name = "applicant_id")
    val applicantId: String? = "-"
)