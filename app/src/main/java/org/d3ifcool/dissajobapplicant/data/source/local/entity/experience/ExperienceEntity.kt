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

    @ColumnInfo(name = "employment_type")
    var employmentType: String? = "-",

    @ColumnInfo(name = "company_name")
    var companyName: String? = "-",

    @ColumnInfo(name = "location")
    var location: String? = "-",

    @ColumnInfo(name = "start_date")
    var startDate: String? = "-",

    @ColumnInfo(name = "end_date")
    var endDate: String? = "-",

    @ColumnInfo(name = "description")
    var description: String? = "-",

    @ColumnInfo(name = "is_currently_working")
    var isCurrentlyWorking: Boolean? = false,

    @NonNull
    @ColumnInfo(name = "applicant_id")
    var applicantId: String
)