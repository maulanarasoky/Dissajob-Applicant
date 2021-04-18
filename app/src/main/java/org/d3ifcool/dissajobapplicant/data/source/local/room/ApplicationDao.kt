package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.paging.DataSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity

interface ApplicationDao {
    @Query("SELECT * FROM applications")
    fun getApplications(): DataSource.Factory<Int, ApplicationEntity>

    @Query("SELECT * FROM applications WHERE status = :status")
    fun getApplicationsByStatus(status: String): DataSource.Factory<Int, ApplicationEntity>

    @Query("SELECT * FROM applications WHERE is_marked = 1")
    fun getMarkedApplications(): DataSource.Factory<Int, ApplicationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApplication(applications: List<ApplicationEntity>)
}