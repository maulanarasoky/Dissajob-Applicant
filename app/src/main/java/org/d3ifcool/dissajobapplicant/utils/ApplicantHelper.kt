package org.d3ifcool.dissajobapplicant.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.recruiter.RecruiterResponseEntity
import org.d3ifcool.dissajobapplicant.ui.applicant.callback.LoadApplicantDetailsCallback
import org.d3ifcool.dissajobapplicant.ui.signin.SignInCallback
import org.d3ifcool.dissajobapplicant.ui.signup.SignUpCallback

object ApplicantHelper {

    private val auth = FirebaseAuth.getInstance()
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users").child("applicants")

    fun signUp(
        email: String,
        password: String,
        applicant: ApplicantResponseEntity,
        callback: SignUpCallback
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { insert ->
            if (insert.isSuccessful) {
                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { verify ->
                    if (verify.isSuccessful) {
                        applicant.id = auth.currentUser?.uid.toString()
                        insertData(applicant, callback)
                        auth.signOut()
                    }
                }
            }
        }
    }

    fun signIn(email: String, password: String, callback: SignInCallback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { check ->
            if (check.isSuccessful) {
                if (auth.currentUser?.isEmailVerified!!) {
                    callback.onSuccess()
                } else {
                    callback.onNotVerified()
                }
            } else {
                callback.onFailure()
            }
        }
    }

    private fun insertData(applicant: ApplicantResponseEntity, callback: SignUpCallback) {
        database.child(applicant.id.toString()).setValue(applicant).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(it.message.toString())
        }
    }

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