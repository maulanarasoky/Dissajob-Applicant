package org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applicants")
data class ApplicantEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "first_name")
    var firstName: String? = "-",

    @ColumnInfo(name = "last_name")
    var lastName: String? = "-",

    @ColumnInfo(name = "full_name")
    var fullName: String? = "-",

    @ColumnInfo(name = "email")
    var email: String? = "-",

    @ColumnInfo(name = "about_me")
    var aboutMe: String? = "-",

    @ColumnInfo(name = "phone_number")
    var phoneNumber: String? = "-",

    @ColumnInfo(name = "image_path")
    var imagePath: String? = "-"
)