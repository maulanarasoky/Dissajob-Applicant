package org.d3ifcool.dissajobapplicant.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.notification.NotificationResponseEntity
import org.d3ifcool.dissajobapplicant.ui.notification.AddNotificationCallback
import org.d3ifcool.dissajobapplicant.ui.notification.LoadNotificationsCallback
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.database.NotificationHelper

class RemoteNotificationSource private constructor(
    private val notificationHelper: NotificationHelper
) {

    companion object {
        @Volatile
        private var instance: RemoteNotificationSource? = null

        fun getInstance(notificationHelper: NotificationHelper): RemoteNotificationSource =
            instance ?: synchronized(this) {
                instance ?: RemoteNotificationSource(notificationHelper)
            }
    }

    fun getNotifications(
        applicantId: String,
        callback: LoadNotificationsCallback
    ): LiveData<ApiResponse<List<NotificationResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultNotifications = MutableLiveData<ApiResponse<List<NotificationResponseEntity>>>()
        notificationHelper.getNotifications(applicantId, object : LoadNotificationsCallback {
            override fun onAllNotificationsReceived(notificationResponse: List<NotificationResponseEntity>): List<NotificationResponseEntity> {
                resultNotifications.value =
                    ApiResponse.success(callback.onAllNotificationsReceived(notificationResponse))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return notificationResponse
            }
        })
        return resultNotifications
    }

    fun addNotification(
        notificationData: NotificationResponseEntity,
        callback: AddNotificationCallback
    ) {
        EspressoIdlingResource.increment()
        notificationHelper.addNotification(notificationData, object : AddNotificationCallback {
            override fun onSuccessAddingNotification() {
                callback.onSuccessAddingNotification()
                EspressoIdlingResource.decrement()
            }

            override fun onFailureAddingNotification(messageId: Int) {
                callback.onFailureAddingNotification(messageId)
                EspressoIdlingResource.decrement()
            }
        })
    }
}