package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM applications WHERE applicant_id = :applicantId")
    fun getApplications(applicantId: String): DataSource.Factory<Int, ApplicationEntity>

    @Query("SELECT * FROM applications WHERE id = :applicationId")
    fun getApplicationById(applicationId: String): LiveData<ApplicationEntity>

    @Query("SELECT * FROM applications WHERE job_id = :jobId AND applicant_id = :applicantId")
    fun getApplicationByJob(jobId: String, applicantId: String): LiveData<ApplicationEntity>

    @Query("SELECT * FROM applications WHERE applicant_id = :applicantId AND status = :status")
    fun getApplicationsByStatus(
        applicantId: String,
        status: String
    ): DataSource.Factory<Int, ApplicationEntity>

    @Query("SELECT * FROM applications WHERE applicant_id = :applicantId AND is_marked = 1")
    fun getMarkedApplications(applicantId: String): DataSource.Factory<Int, ApplicationEntity>

    @Query("DELETE FROM applications WHERE applicant_id = :applicantId")
    fun deleteAllApplications(applicantId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApplication(applications: List<ApplicationEntity>)
}