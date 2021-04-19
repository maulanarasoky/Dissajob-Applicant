package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity

interface RecruiterDao {
    @Query("SELECT * FROM recruiters WHERE id = :userId")
    fun getRecruiterData(userId: String): LiveData<RecruiterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecruiterData(recruiterProfile: RecruiterEntity)
}