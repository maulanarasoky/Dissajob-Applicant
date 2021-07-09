package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity

@Dao
interface RecruiterDao {
    @Query("SELECT * FROM recruiters WHERE id = :recruiterId")
    fun getRecruiterData(recruiterId: String): LiveData<RecruiterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecruiterData(recruiterProfile: RecruiterEntity)
}