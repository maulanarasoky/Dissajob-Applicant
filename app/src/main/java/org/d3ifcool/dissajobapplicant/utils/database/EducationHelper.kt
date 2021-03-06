package org.d3ifcool.dissajobapplicant.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobapplicant.ui.education.callback.AddEducationCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.DeleteEducationCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.LoadEducationsCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.UpdateEducationCallback

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
                                data.child("educationLevel").value.toString(),
                                data.child("fieldOfStudy").value.toString(),
                                data.child("startMonth").value.toString().toInt(),
                                data.child("startYear").value.toString().toInt(),
                                data.child("endMonth").value.toString().toInt(),
                                data.child("endYear").value.toString().toInt(),
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
        callback: AddEducationCallback
    ) {
        val id = database.push().key
        education.id = id.toString()
        database.child(education.id).setValue(education).addOnSuccessListener {
            callback.onSuccessAdding()
        }.addOnFailureListener {
            callback.onFailureAdding(R.string.txt_failure_insert)
        }
    }

    fun updateApplicantEducation(
        education: EducationResponseEntity,
        callback: UpdateEducationCallback
    ) {
        database.child(education.id).setValue(education).addOnSuccessListener {
            callback.onSuccessUpdate()
        }.addOnFailureListener {
            callback.onFailureUpdate(R.string.txt_failure_update)
        }
    }

    fun deleteApplicantEducation(
        id: String,
        callback: DeleteEducationCallback
    ) {
        database.child(id).removeValue().addOnSuccessListener {
            callback.onSuccessDelete()
        }.addOnFailureListener {
            callback.onFailureDelete(R.string.txt_failure_delete)
        }
    }
}