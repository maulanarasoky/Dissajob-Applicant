package org.d3ifcool.dissajobapplicant.ui.notification

interface AddNotificationCallback {
    fun onSuccessAddingNotification()
    fun onFailureAddingNotification(messageId: Int)
}