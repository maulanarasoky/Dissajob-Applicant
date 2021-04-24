package org.d3ifcool.dissajobapplicant.data.source.local.entity.job

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "title")
    val title: String? = "-",

    @ColumnInfo(name = "address")
    val address: String? = "-",

    @ColumnInfo(name = "posted_by")
    val postedBy: String? = "-",

    @ColumnInfo(name = "posted_date")
    val postedDate: String? = "-",

    @ColumnInfo(name = "is_open")
    val isOpen: Boolean? = true
)