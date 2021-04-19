package org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recruiters")
data class RecruiterEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "first_name")
    val firstName: String? = "-",

    @ColumnInfo(name = "last_name")
    val lastName: String? = "-",

    @ColumnInfo(name = "full_name")
    val fullName: String? = "-",

    @ColumnInfo(name = "email")
    val email: String? = "-",

    @ColumnInfo(name = "address")
    val address: String? = "-",

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String? = "-",

    @ColumnInfo(name = "role")
    val role: String? = "-",

    @ColumnInfo(name = "image_path")
    val imagePath: String? = "-"
)