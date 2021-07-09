package org.d3ifcool.dissajobapplicant.data.source.repository.notification

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.notification.NotificationEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.notification.NotificationResponseEntity
import org.d3ifcool.dissajobapplicant.ui.notification.AddNotificationCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface NotificationDataSource {
    fun getNotifications(applicantId: String): LiveData<Resource<PagedList<NotificationEntity>>>
    fun addNotification(notificationData: NotificationResponseEntity, callback: AddNotificationCallback)
}