package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.interview.InterviewEntity

@Dao
interface InterviewDao {
    @Query("SELECT * FROM interview WHERE job_id = :jobId")
    fun getInterviewAnswers(jobId: String): DataSource.Factory<Int, InterviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInterviewAnswers(answers: List<InterviewEntity>)
}