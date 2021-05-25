package org.d3ifcool.dissajobapplicant.data.source.local.entity.experience

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "experiences")
data class ExperienceEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "title")
    var title: String? = "-",

    @ColumnInfo(name = "employment_type")
    var employmentType: String? = "-",

    @ColumnInfo(name = "company_name")
    var companyName: String? = "-",

    @ColumnInfo(name = "location")
    var location: String? = "-",

    @ColumnInfo(name = "start_month")
    var startMonth: String? = "-",

    @ColumnInfo(name = "start_year")
    var startYear: String? = "-",

    @ColumnInfo(name = "end_month")
    var endMonth: String? = "-",

    @ColumnInfo(name = "end_year")
    var endYear: String? = "-",

    @ColumnInfo(name = "description")
    var description: String? = "-",

    @ColumnInfo(name = "is_currently_working")
    var isCurrentlyWorking: Boolean? = false,

    @NonNull
    @ColumnInfo(name = "applicant_id")
    var applicantId: String
)