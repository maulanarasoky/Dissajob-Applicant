package org.d3ifcool.dissajobapplicant.data.source.local.source

import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.notification.NotificationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.room.NotificationDao

class LocalNotificationSource private constructor(
    private val mNotificationDao: NotificationDao
) {

    companion object {
        private var INSTANCE: LocalNotificationSource? = null

        fun getInstance(notificationDao: NotificationDao): LocalNotificationSource =
            INSTANCE ?: LocalNotificationSource(notificationDao)
    }

    fun getNotifications(applicantId: String): DataSource.Factory<Int, NotificationEntity> =
        mNotificationDao.getNotifications(applicantId)

    fun insertNotifications(notifications: List<NotificationEntity>) =
        mNotificationDao.insertNotifications(notifications)
}