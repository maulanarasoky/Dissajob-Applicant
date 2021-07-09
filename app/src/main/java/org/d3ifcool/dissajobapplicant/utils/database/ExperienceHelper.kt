package org.d3ifcool.dissajobapplicant.utils.database

import com.google.firebase.database.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.experience.ExperienceResponseEntity
import org.d3ifcool.dissajobapplicant.ui.experience.callback.AddExperienceCallback
import org.d3ifcool.dissajobapplicant.ui.experience.callback.DeleteExperienceCallback
import org.d3ifcool.dissajobapplicant.ui.experience.callback.LoadExperiencesCallback
import org.d3ifcool.dissajobapplicant.ui.experience.callback.UpdateExperienceCallback

object ExperienceHelper {
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("experiences")
    private val arrExperience: MutableList<ExperienceResponseEntity> = mutableListOf()

    fun getApplicantExperiences(applicantId: String, callback: LoadExperiencesCallback) {
        database.orderByChild("applicantId").equalTo(applicantId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrExperience.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val experience = ExperienceResponseEntity(
                                data.key.toString(),
                                data.child("title").value.toString(),
                                data.child("employmentType").value.toString(),
                                data.child("companyName").value.toString(),
                                data.child("location").value.toString(),
                                data.child("startMonth").value.toString().toInt(),
                                data.child("startYear").value.toString().toInt(),
                                data.child("endMonth").value.toString().toInt(),
                                data.child("endYear").value.toString().toInt(),
                                data.child("description").value.toString(),
                                data.child("currentlyWorking").value.toString().toBoolean(),
                                data.child("applicantId").value.toString()
                            )
                            arrExperience.add(experience)
                        }
                    }
                    callback.onAllExperiencesReceived(arrExperience)
                }

            })
    }

    fun addApplicantExperience(
        experience: ExperienceResponseEntity,
        callback: AddExperienceCallback
    ) {
        val id = database.push().key
        experience.id = id.toString()
        database.child(experience.id).setValue(experience).addOnSuccessListener {
            callback.onSuccessAdding()
        }.addOnFailureListener {
            callback.onFailureAdding(R.string.txt_failure_update)
        }
    }

    fun updateApplicantExperience(
        experience: ExperienceResponseEntity,
        callback: UpdateExperienceCallback
    ) {
        database.child(experience.id).setValue(experience).addOnSuccessListener {
            callback.onSuccessUpdate()
        }.addOnFailureListener {
            callback.onFailureUpdate(R.string.txt_failure_update)
        }
    }

    fun deleteApplicantExperience(
        id: String,
        callback: DeleteExperienceCallback
    ) {
        database.child(id).removeValue().addOnSuccessListener {
            callback.onSuccessDelete()
        }.addOnFailureListener {
            callback.onFailureDelete(R.string.txt_failure_delete)
        }
    }
}