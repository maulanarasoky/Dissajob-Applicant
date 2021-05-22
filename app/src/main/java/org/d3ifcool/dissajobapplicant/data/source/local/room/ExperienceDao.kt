package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity

@Dao
interface ExperienceDao {
    @Query("SELECT * FROM experiences WHERE applicant_id = :applicantId")
    fun getApplicantExperiences(applicantId: String): DataSource.Factory<Int, ExperienceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApplicantExperiences(experiences: List<ExperienceEntity>)
}