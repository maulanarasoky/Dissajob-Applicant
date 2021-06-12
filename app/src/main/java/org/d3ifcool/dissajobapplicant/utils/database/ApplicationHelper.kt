package org.d3ifcool.dissajobapplicant.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobapplicant.ui.application.callback.LoadAllApplicationsCallback
import org.d3ifcool.dissajobapplicant.ui.application.callback.LoadApplicationDataCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.ApplyJobCallback

object ApplicationHelper {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("applications")
    private val arr_application: MutableList<ApplicationResponseEntity> = mutableListOf()

    fun getAllApplications(callback: LoadAllApplicationsCallback) {
        database.orderByChild("applyDate")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arr_application.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val application = ApplicationResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applyDate").value.toString(),
                                data.child("updatedDate").value.toString(),
                                data.child("status").value.toString(),
                                data.child("marked").value.toString().toBoolean()
                            )
                            arr_application.add(application)
                        }
                    }
                    callback.onAllApplicationsReceived(arr_application)
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun getApplicationById(applicationId: String, callback: LoadApplicationDataCallback) {
        database.child(applicationId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val application = ApplicationResponseEntity(
                        dataSnapshot.key.toString(),
                        dataSnapshot.child("applicantId").value.toString(),
                        dataSnapshot.child("jobId").value.toString(),
                        dataSnapshot.child("applyDate").value.toString(),
                        dataSnapshot.child("updatedDate").value.toString(),
                        dataSnapshot.child("status").value.toString(),
                        dataSnapshot.child("marked").value.toString().toBoolean()
                    )
                    callback.onApplicationDataReceived(application)
                }
            }

        })
    }

    fun getApplicationByJob(
        jobId: String,
        applicantId: String,
        callback: LoadApplicationDataCallback
    ) {
        database.orderByChild("applicantId").equalTo(applicantId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            if (data.child("jobId").value.toString() == jobId) {
                                val application = ApplicationResponseEntity(
                                    data.key.toString(),
                                    data.child("applicantId").value.toString(),
                                    data.child("jobId").value.toString(),
                                    data.child("applyDate").value.toString(),
                                    data.child("updatedDate").value.toString(),
                                    data.child("status").value.toString(),
                                    data.child("marked").value.toString().toBoolean()
                                )
                                callback.onApplicationDataReceived(application)
                                return
                            }
                        }
                    }
                }

            })
    }

    fun getAllApplicationsByStatus(status: String, callback: LoadAllApplicationsCallback) {
        database.orderByChild("status").equalTo(status)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arr_application.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val application = ApplicationResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applyDate").value.toString(),
                                data.child("updatedDate").value.toString(),
                                data.child("status").value.toString(),
                                data.child("marked").value.toString().toBoolean()
                            )
                            arr_application.add(application)
                        }
                    }
                    callback.onAllApplicationsReceived(arr_application)
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun getMarkedApplications(callback: LoadAllApplicationsCallback) {
        database.orderByChild("is_marked").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arr_application.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val application = ApplicationResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applyDate").value.toString(),
                                data.child("updatedDate").value.toString(),
                                data.child("status").value.toString(),
                                data.child("marked").value.toString().toBoolean()
                            )
                            arr_application.add(application)
                        }
                    }
                    callback.onAllApplicationsReceived(arr_application)
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun insertApplication(application: ApplicationResponseEntity, callback: ApplyJobCallback) {
        application.id = database.push().key.toString()
        application.applicantId = AuthHelper.currentUser?.uid.toString()
        database.child(application.id)
            .setValue(application).addOnSuccessListener {
                callback.onSuccessApply(application.id)
            }.addOnFailureListener {
                callback.onFailureApply(R.string.txt_success_apply)
            }
    }
}