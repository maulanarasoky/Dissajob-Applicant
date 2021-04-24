package org.d3ifcool.dissajobapplicant.utils

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.SavedJobResponseEntity
import org.d3ifcool.dissajobapplicant.ui.application.callback.LoadAllApplicationsCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.ApplyJobCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.SaveJobCallback

object ApplicationHelper {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("applications")
    private val arr_application: MutableList<ApplicationResponseEntity> = mutableListOf()

    fun getAllApplications(callback: LoadAllApplicationsCallback) {
        database.orderByChild("applyDate")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arr_application.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val application = ApplicationResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applyDate").value.toString(),
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

    fun getAllApplicationsByStatus(status: String, callback: LoadAllApplicationsCallback) {
        database.orderByChild("status").equalTo(status)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arr_application.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val application = ApplicationResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applyDate").value.toString(),
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
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arr_application.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val application = ApplicationResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applyDate").value.toString(),
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
        database.child(application.id.toString())
            .setValue(application).addOnSuccessListener {
                callback.onSuccess()
            }.addOnFailureListener {
                callback.onFailure(R.string.txt_failure_update)
            }
    }
}