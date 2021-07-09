package org.d3ifcool.dissajobapplicant.ui.notification

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.notification.NotificationResponseEntity

interface LoadNotificationsCallback {
    fun onAllNotificationsReceived(notificationResponse: List<NotificationResponseEntity>): List<NotificationResponseEntity>
}