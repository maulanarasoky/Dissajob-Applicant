package org.d3ifcool.dissajobapplicant.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.interview.InterviewResponseEntity
import org.d3ifcool.dissajobapplicant.ui.question.InsertInterviewAnswersCallback
import org.d3ifcool.dissajobapplicant.ui.question.LoadInterviewAnswersCallback

object InterviewHelper {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("interview")
    private val arr_interview: MutableList<InterviewResponseEntity> = mutableListOf()

    fun getInterviewAnswers(jobId: String, callback: LoadInterviewAnswersCallback) {
        database.orderByChild("jobId").equalTo(jobId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arr_interview.clear()
                    if (snapshot.exists()) {
                        for (data in snapshot.children.reversed()) {
                            val interviewAnswer = InterviewResponseEntity(
                                data.key.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("jobId").value.toString(),
                                data.child("firstAnswer").value.toString(),
                                data.child("secondAnswer").value.toString(),
                                data.child("thirdAnswer").value.toString()
                            )
                            arr_interview.add(interviewAnswer)
                        }
                    }
                    callback.onAllInterviewAnswersReceived(arr_interview)
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun insertInterviewAnswer(
        interviewAnswers: InterviewResponseEntity,
        callback: InsertInterviewAnswersCallback
    ) {
        database.child(interviewAnswers.id)
            .setValue(interviewAnswers).addOnSuccessListener {
                callback.onSuccess()
            }.addOnFailureListener {
                callback.onFailure(R.string.txt_failure_update)
            }
    }

}