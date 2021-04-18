package org.d3ifcool.dissajobapplicant.data.source.local.entity.job

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "job_details")
data class JobDetailsEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "title")
    val title: String? = "-",

    @ColumnInfo(name = "description")
    val description: String? = "-",

    @ColumnInfo(name = "qualification")
    val qualification: String? = "-",

    @ColumnInfo(name = "employment")
    val employment: String? = "-",

    @ColumnInfo(name = "industry")
    val industry: String? = "-",

    @ColumnInfo(name = "salary")
    val salary: Int? = 0,

    @ColumnInfo(name = "posted_by")
    var postedBy: String? = "-",

    @ColumnInfo(name = "posted_date")
    val postedDate: String? = "-",

    @ColumnInfo(name = "updated_date")
    val updatedDate: String? = "-",

    @ColumnInfo(name = "closed_date")
    val closedDate: String? = "-",

    @ColumnInfo(name = "is_open")
    val isOpen: Boolean? = true
): Parcelable