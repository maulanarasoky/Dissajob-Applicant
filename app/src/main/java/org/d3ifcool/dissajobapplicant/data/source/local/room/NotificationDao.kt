package org.d3ifcool.dissajobapplicant.data.source.local.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.d3ifcool.dissajobapplicant.data.source.local.entity.notification.NotificationEntity

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE applicant_id = :applicantId")
    fun getNotifications(applicantId: String): DataSource.Factory<Int, NotificationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotifications(notifications: List<NotificationEntity>)
}