package org.d3ifcool.dissajobapplicant.utils

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.ui.applicant.callback.LoadApplicantDetailsCallback

object ApplicantHelper {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users").child("applicants")

    fun getApplicantDetails(
        applicantId: String,
        callback: LoadApplicantDetailsCallback
    ) {
        database.child(applicantId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val applicantDetails =
                            snapshot.getValue<ApplicantResponseEntity>() ?: return
                        applicantDetails.id = snapshot.key ?: return
                        callback.onApplicantDetailsReceived(applicantDetails)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}