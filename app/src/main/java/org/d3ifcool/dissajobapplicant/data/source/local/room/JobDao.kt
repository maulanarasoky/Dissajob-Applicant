package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.SavedJobEntity

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs WHERE is_open = 1")
    fun getJobs(): DataSource.Factory<Int, JobEntity>

    @Query("SELECT * FROM saved_jobs WHERE applicant_id = :applicantId")
    fun getSavedJobs(applicantId: String): DataSource.Factory<Int, SavedJobEntity>

    @Query("SELECT * FROM saved_jobs WHERE job_id = :jobId AND applicant_id = :applicantId")
    fun getSavedJobByJob(jobId: String, applicantId: String): LiveData<SavedJobEntity>

    @Query("SELECT * FROM jobs WHERE id = :jobId")
    fun getJobById(jobId: String): LiveData<JobEntity>

    @Query("SELECT * FROM jobs WHERE posted_by = :recruiterId")
    fun getJobsByRecruiter(recruiterId: String): DataSource.Factory<Int, JobEntity>

    @Query("SELECT * FROM job_details WHERE id = :jobId")
    fun getJobDetails(jobId: String): LiveData<JobDetailsEntity>

    @Query("SELECT * FROM jobs WHERE title LIKE '%' || :searchText || '%' OR address LIKE '%' || :searchText || '%'")
    fun searchJob(searchText: String): DataSource.Factory<Int, JobEntity>

    @Query("SELECT * FROM jobs WHERE (title LIKE '%' || :searchText || '%' OR address LIKE '%' || :searchText || '%') AND is_open_for_disability = 1")
    fun getFilteredJobs(searchText: String): DataSource.Factory<Int, JobEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJobs(jobs: List<JobEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSavedJobs(jobs: List<SavedJobEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJobDetails(jobDetails: JobDetailsEntity)

    @Query("DELETE FROM jobs")
    fun deleteAllJobs()

    @Query("DELETE FROM jobs WHERE posted_by = :recruiterId")
    fun deleteAllJobsByRecruiter(recruiterId: String)

    @Query("DELETE FROM saved_jobs WHERE applicant_id = :applicantId")
    fun deleteAllSavedJobs(applicantId: String)

    @Query("DELETE FROM saved_jobs WHERE job_id = :jobId AND applicant_id = :applicantId")
    fun deleteCurrentSavedJob(jobId: String, applicantId: String)

    @Query("DELETE FROM saved_jobs WHERE id = :id")
    fun unSaveJob(id: String)
}