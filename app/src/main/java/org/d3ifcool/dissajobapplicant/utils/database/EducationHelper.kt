package org.d3ifcool.dissajobapplicant.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobapplicant.ui.education.LoadEducationsCallback
import org.d3ifcool.dissajobapplicant.utils.InsertToDatabaseCallback

object EducationHelper {
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("educations")
    private val arrEducation: MutableList<EducationResponseEntity> = mutableListOf()

    fun getApplicantEducations(applicantId: String, callback: LoadEducationsCallback) {
        database.orderByChild("applicantId").equalTo(applicantId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrEducation.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val education = EducationResponseEntity(
                                data.key.toString(),
                                data.child("schoolName").value.toString(),
                                data.child("degree").value.toString(),
                                data.child("fieldOfStudy").value.toString(),
                                data.child("startDate").value.toString(),
                                data.child("endDate").value.toString(),
                                data.child("description").value.toString(),
                                data.child("applicantId").value.toString()
                            )
                            arrEducation.add(education)
                        }
                    }
                    callback.onAllEducationsReceived(arrEducation)
                }

            })
    }

    fun addApplicantEducation(
        education: EducationResponseEntity,
        callback: InsertToDatabaseCallback
    ) {
        val id = database.push().key
        education.id = id.toString()
        education.applicantId = AuthHelper.currentUser?.uid.toString()
        database.child(education.id.toString()).setValue(education).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(R.string.txt_failure_update)
        }
    }
}