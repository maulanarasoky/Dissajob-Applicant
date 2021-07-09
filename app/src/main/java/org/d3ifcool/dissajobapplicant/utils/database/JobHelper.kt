package org.d3ifcool.dissajobapplicant.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.JobDetailsResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.JobResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.SavedJobResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteJobSource
import org.d3ifcool.dissajobapplicant.ui.job.callback.*
import org.d3ifcool.dissajobapplicant.ui.job.savedjob.LoadSavedJobDataCallback
import java.util.*

object JobHelper {

    private val jobDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("jobs")
    private val arrJob: MutableList<JobResponseEntity> = mutableListOf()

    private val savedJobDatabase = FirebaseDatabase.getInstance().getReference("saved_job")
    private val arrSavedJob: MutableList<SavedJobResponseEntity> = mutableListOf()

    fun getJobs(callback: LoadJobsCallback) {
        jobDatabase.orderByChild("open").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
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
                                data.child("open").value.toString().toBoolean(),
                                data.child("openForDisability").value.toString().toBoolean()
                            )
                            arrJob.add(job)
                        }
                    }
                    callback.onAllJobsReceived(arrJob)
                }

            })
    }

    fun getSavedJobs(applicantId: String, callback: LoadSavedJobsCallback) {
        savedJobDatabase.orderByChild("applicantId").equalTo(applicantId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrSavedJob.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val job = SavedJobResponseEntity(
                                data.key.toString(),
                                data.child("jobId").value.toString(),
                                data.child("applicantId").value.toString()
                            )
                            arrSavedJob.add(job)
                        }
                    }
                    callback.onAllJobsReceived(arrSavedJob)
                }

            })
    }

    fun getSavedJobByJob(jobId: String, applicantId: String, callback: LoadSavedJobDataCallback) {
        savedJobDatabase.orderByChild("applicantId").equalTo(applicantId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            if (data.child("jobId").value.toString() == jobId) {
                                val job = SavedJobResponseEntity(
                                    data.key.toString(),
                                    data.child("jobId").value.toString(),
                                    data.child("applicantId").value.toString()
                                )
                                callback.onSavedJobDataCallback(job)
                                break
                            }
                        }
                    }
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
                        dataSnapshot.child("open").value.toString().toBoolean(),
                        dataSnapshot.child("openForDisability").value.toString().toBoolean()
                    )
                    callback.onJobDataReceived(job)
                }
            }

        })
    }

    fun getJobsByRecruiter(recruiterId: String, callback: LoadJobsCallback) {
        jobDatabase.orderByChild("postedBy").equalTo(recruiterId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
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
                                data.child("open").value.toString().toBoolean(),
                                data.child("openForDisability").value.toString().toBoolean()
                            )
                            arrJob.add(job)
                        }
                    }
                    callback.onAllJobsReceived(arrJob)
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
                        dataSnapshot.child("open").value.toString().toBoolean(),
                        dataSnapshot.child("openForDisability").value.toString().toBoolean(),
                        dataSnapshot.child("additionalInformation").value.toString()
                    )
                    callback.onJobDetailsReceived(jobDetails)
                }
            }

        })
    }

    fun saveJob(savedJob: SavedJobResponseEntity, callback: SaveJobCallback) {
        savedJob.id = savedJobDatabase.push().key.toString()
        savedJobDatabase.child(savedJob.id).setValue(savedJob).addOnSuccessListener {
            callback.onSuccessSave(savedJob.id)
        }.addOnFailureListener {
            callback.onFailureSave(R.string.txt_failure_update)
        }
    }

    fun unSaveJob(id: String, callback: UnSaveJobCallback) {
        savedJobDatabase.child(id).removeValue().addOnSuccessListener {
            callback.onSuccessUnSave()
        }.addOnFailureListener {
            callback.onFailureUnSave(R.string.txt_failure_delete)
        }
    }

    fun searchJob(searchText: String, callback: LoadJobsCallback) {
        jobDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arrJob.clear()
                val text = searchText.toLowerCase(Locale.getDefault())
                for (data in dataSnapshot.children) {
                    val job = JobResponseEntity(
                        data.key.toString(),
                        data.child("title").value.toString(),
                        data.child("address").value.toString(),
                        data.child("postedBy").value.toString(),
                        data.child("postedDate").value.toString(),
                        data.child("open").value.toString().toBoolean(),
                        data.child("openForDisability").value.toString().toBoolean()
                    )
                    when {
                        job.title?.toLowerCase(Locale.ROOT)?.contains(text) == true -> {
                            arrJob.add(job)
                        }
                        job.address?.toLowerCase(Locale.ROOT)?.contains(text) == true -> {
                            arrJob.add(job)
                        }
                    }
                }
                callback.onAllJobsReceived(arrJob)
            }
        })
    }
}