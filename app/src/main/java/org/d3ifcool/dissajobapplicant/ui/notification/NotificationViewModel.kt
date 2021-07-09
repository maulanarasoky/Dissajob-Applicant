package org.d3ifcool.dissajobapplicant.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.notification.NotificationEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.notification.NotificationResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.notification.NotificationRepository
import org.d3ifcool.dissajobapplicant.vo.Resource

class NotificationViewModel(private val notificationRepository: NotificationRepository) :
    ViewModel() {
    fun getNotifications(applicantId: String): LiveData<Resource<PagedList<NotificationEntity>>> =
        notificationRepository.getNotifications(applicantId)

    fun addNotification(
        notificationData: NotificationResponseEntity,
        callback: AddNotificationCallback
    ) = notificationRepository.addNotification(notificationData, callback)
}