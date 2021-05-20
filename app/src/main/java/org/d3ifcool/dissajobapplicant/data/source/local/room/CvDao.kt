package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.paging.DataSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.cv.CvEntity

interface CvDao {
    @Query("SELECT * FROM cv WHERE applicant_id = :applicantId")
    fun getApplicantCv(applicantId: String): DataSource.Factory<Int, CvEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCv(cv: List<CvEntity>)
}