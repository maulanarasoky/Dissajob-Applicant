package org.d3ifcool.dissajobapplicant.data.source.local.entity.education

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "educations")
data class EducationEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "school_name")
    var schoolName: String? = "-",

    @ColumnInfo(name = "degree")
    var degree: String? = "-",

    @ColumnInfo(name = "field_of_study")
    var fieldOfStudy: String? = "-",

    @ColumnInfo(name = "start_date")
    var startDate: String? = "-",

    @ColumnInfo(name = "end_date")
    var endDate: String? = "-",

    @ColumnInfo(name = "description")
    var description: String? = "-",

    @NonNull
    @ColumnInfo(name = "applicant_id")
    var applicantId: String
)