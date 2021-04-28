package org.d3ifcool.dissajobapplicant.utils

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.SavedJobResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobapplicant.ui.job.callback.*

object JobHelper {

    private val jobDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("jobs")
    private val arrJob: MutableList<JobResponseEntity> = mutableListOf()

    private val savedJobDatabase = FirebaseDatabase.getInstance().getReference("saved_job")
    private val arrSavedJob: MutableList<SavedJobResponseEntity> = mutableListOf()

    fun getJobs(callback: LoadJobsCallback) {
        jobDatabase.orderByChild("open").equalTo(true)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrJob.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val job = JobResponseEntity(
                                data.key.toString(),
                                data.child("title").value.toString(),
                                data.child("address").value.toString(),
                                data.child("postedBy").value.toString(),
                                data.child("postedDate").value.toString(),
                                data.child("open").value.toString().toBoolean()
                            )
                            arrJob.add(job)
                        }
                    }
                    callback.onAllJobsReceived(arrJob)
                }

            })
    }

    fun getSavedJobs(callback: LoadSavedJobsCallback) {
        savedJobDatabase.child(AuthHelper.currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrSavedJob.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val job = SavedJobResponseEntity(
                                data.key.toString(),
                                data.child("jobId").value.toString()
                            )
                            arrSavedJob.add(job)
                        }
                    }
                    callback.onAllJobsReceived(arrSavedJob)
                }

            })
    }

    fun getJobById(jobId: String, callback: RemoteJobSource.LoadJobDataCallback) {
        jobDatabase.child(jobId).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val job = JobResponseEntity(
                            dataSnapshot.key.toString(),
                            dataSnapshot.child("title").value.toString(),
                            dataSnapshot.child("address").value.toString(),
                            dataSnapshot.child("postedBy").value.toString(),
                            dataSnapshot.child("postedDate").value.toString(),
                            dataSnapshot.child("open").value.toString().toBoolean()
                        )
                        callback.onJobDataReceived(job)
                    }
                }

            })
    }

    fun getJobDetails(jobId: String, callback: LoadJobDetailsCallback) {
        jobDatabase.child(jobId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val jobDetails = JobDetailsResponseEntity(
                        dataSnapshot.key.toString(),
                        dataSnapshot.child("title").value.toString(),
                        dataSnapshot.child("description").value.toString(),
                        dataSnapshot.child("address").value.toString(),
                        dataSnapshot.child("qualification").value.toString(),
                        dataSnapshot.child("employment").value.toString(),
                        dataSnapshot.child("type").value.toString(),
                        dataSnapshot.child("industry").value.toString(),
                        dataSnapshot.child("salary").value.toString(),
                        dataSnapshot.child("postedBy").value.toString(),
                        dataSnapshot.child("postedDate").value.toString(),
                        dataSnapshot.child("updatedDate").value.toString(),
                        dataSnapshot.child("closedDate").value.toString(),
                        dataSnapshot.child("open").value.toString().toBoolean()
                    )
                    callback.onJobDetailsReceived(jobDetails)
                }
            }

        })
    }

    fun saveJob(savedJob: SavedJobResponseEntity, callback: SaveJobCallback) {
        savedJobDatabase.child(AuthHelper.currentUser?.uid.toString()).child(savedJob.id.toString())
            .setValue(savedJob).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(R.string.txt_failure_update)
        }
    }
}