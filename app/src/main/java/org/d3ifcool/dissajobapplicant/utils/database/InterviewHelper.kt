package org.d3ifcool.dissajobapplicant.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.interview.InterviewResponseEntity
import org.d3ifcool.dissajobapplicant.ui.question.InsertInterviewAnswersCallback
import org.d3ifcool.dissajobapplicant.ui.question.LoadInterviewAnswersCallback

object InterviewHelper {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("interview")

    fun getInterviewAnswers(applicationId: String, callback: LoadInterviewAnswersCallback) {
        database.orderByChild("applicationId").equalTo(applicationId).limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val interviewAnswer = InterviewResponseEntity(
                                data.key.toString(),
                                data.child("applicationId").value.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("firstAnswer").value.toString(),
                                data.child("secondAnswer").value.toString(),
                                data.child("thirdAnswer").value.toString()
                            )
                            callback.onAllInterviewAnswersReceived(interviewAnswer)
                            break
                        }
                    }
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

            })
    }

    fun insertInterviewAnswer(
        interviewAnswers: InterviewResponseEntity,
        callback: InsertInterviewAnswersCallback
    ) {
        interviewAnswers.id = database.push().key.toString()
        database.child(interviewAnswers.id)
            .setValue(interviewAnswers).addOnSuccessListener {
                callback.onSuccessAdding()
            }.addOnFailureListener {
                callback.onFailureAdding(R.string.txt_failure_apply)
            }
    }

}