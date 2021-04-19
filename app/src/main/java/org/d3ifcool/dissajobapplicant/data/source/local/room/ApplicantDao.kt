package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity

interface ApplicantDao {
    @Query("SELECT * FROM applicants WHERE id = :applicantId")
    fun getApplicantData(applicantId: String): LiveData<ApplicantEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApplicantData(applicant: ApplicantEntity)
}