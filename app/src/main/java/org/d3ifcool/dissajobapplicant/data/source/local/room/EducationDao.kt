package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity

@Dao
interface EducationDao {
    @Query("SELECT * FROM educations WHERE applicant_id = :applicantId")
    fun getApplicantEducations(applicantId: String): DataSource.Factory<Int, EducationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApplicantEducations(educations: List<EducationEntity>)

    @Query("DELETE FROM educations WHERE id = :id")
    fun deleteApplicantEducation(id: String)
}