package org.d3ifcool.dissajobapplicant.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.notification.NotificationResponseEntity
import org.d3ifcool.dissajobapplicant.ui.notification.AddNotificationCallback
import org.d3ifcool.dissajobapplicant.ui.notification.LoadNotificationsCallback

object NotificationHelper {
    private val applicantNotification: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("applicant_notifications")
    private val recruiterNotification: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("recruiter_notifications")
    private val arrNotification: MutableList<NotificationResponseEntity> = mutableListOf()

    fun getNotifications(applicantId: String, callback: LoadNotificationsCallback) {
        applicantNotification.orderByChild("applicantId").equalTo(applicantId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrNotification.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val notification = NotificationResponseEntity(
                                data.key.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applicationId").value.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("recruiterId").value.toString(),
                                data.child("notificationDate").value.toString(),
                            )
                            arrNotification.add(notification)
                        }
                    }
                    callback.onAllNotificationsReceived(arrNotification)
                }

            })
    }

    fun addNotification(
        notificationData: NotificationResponseEntity,
        callback: AddNotificationCallback
    ) {
        notificationData.id = recruiterNotification.push().key.toString()
        recruiterNotification.child(notificationData.id).setValue(notificationData).addOnSuccessListener {
            callback.onSuccessAddingNotification()
        }.addOnFailureListener {
            callback.onFailureAddingNotification(R.string.txt_error_occurred)
        }
    }
}