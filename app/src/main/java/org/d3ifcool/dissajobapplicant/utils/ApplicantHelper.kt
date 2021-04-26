package org.d3ifcool.dissajobapplicant.utils

import android.net.Uri
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.ui.profile.callback.LoadApplicantDetailsCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UpdateProfileCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadProfilePictureCallback
import org.d3ifcool.dissajobapplicant.ui.resetpassword.ResetPasswordCallback
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
            } else {
                callback.onFailure(R.string.alert_email_not_available)
            }
        }
    }

    fun signIn(email: String, password: String, callback: SignInCallback) {
        database.orderByChild("email").equalTo(email).limitToFirst(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        checkSignIn(email, password, callback)
                    } else {
                        callback.onFailure()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun checkSignIn(email: String, password: String, callback: SignInCallback) {
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
            callback.onFailure(R.string.alert_email_not_available)
        }
    }

    fun getApplicantData(
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

    fun updateApplicantData(applicant: ApplicantResponseEntity, callback: UpdateProfileCallback) {
        database.child(applicant.id.toString()).setValue(applicant).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(R.string.txt_failure_update)
        }
    }

    fun uploadApplicantProfilePicture(image: Uri, callback: UploadProfilePictureCallback) {
        val storageRef = Firebase.storage.reference
        val imageId = database.push().key
        val uploadImage = storageRef.child("applicant/profile/images/${imageId}").putFile(image)
        uploadImage.addOnSuccessListener {
            callback.onSuccessUpload(imageId.toString())
        }.addOnFailureListener {
            callback.onFailureUpload(R.string.txt_failure_upload_profile_picture)
        }
    }

    fun updateApplicantEmail(
        userId: String,
        newEmail: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        auth.signInWithEmailAndPassword(auth.currentUser?.email.toString(), password)
            .addOnSuccessListener {
                updateEmailAuthentication(userId, newEmail, password, callback)
            }
            .addOnFailureListener {
                callback.onFailure(R.string.txt_wrong_password)
            }
    }

    private fun updateEmailAuthentication(
        userId: String,
        newEmail: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        val credential =
            EmailAuthProvider.getCredential(auth.currentUser?.email.toString(), password)
        auth.currentUser?.reauthenticate(credential)
            ?.addOnCompleteListener {
                auth.currentUser!!.updateEmail(newEmail)
                    .addOnSuccessListener {
                        storeNewEmail(userId, newEmail, callback)
                    }
                    .addOnFailureListener {
                        callback.onFailure(R.string.alert_email_not_available)
                    }
            }
    }

    private fun storeNewEmail(userId: String, newEmail: String, callback: UpdateProfileCallback) {
        database.child(userId).child("email").setValue(newEmail).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(R.string.txt_failure_update)
        }
    }

    fun updateApplicantPhoneNumber(
        userId: String,
        newPhoneNumber: String,
        password: String,
        callback: UpdateProfileCallback
    ) {
        auth.signInWithEmailAndPassword(auth.currentUser?.email.toString(), password)
            .addOnSuccessListener {
                storeNewPhoneNumber(userId, newPhoneNumber, callback)
            }
            .addOnFailureListener {
                callback.onFailure(R.string.txt_wrong_password)
            }
    }

    private fun storeNewPhoneNumber(
        userId: String,
        newPhoneNumber: String,
        callback: UpdateProfileCallback
    ) {
        database.child(userId).child("phoneNumber").setValue(newPhoneNumber).addOnSuccessListener {
            callback.onSuccess()
        }.addOnFailureListener {
            callback.onFailure(R.string.txt_failure_update)
        }
    }

    fun updateApplicantPassword(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) {
        auth.signInWithEmailAndPassword(email, oldPassword)
            .addOnSuccessListener {
                storeNewPassword(email, oldPassword, newPassword, callback)
            }
            .addOnFailureListener {
                callback.onFailure(R.string.txt_wrong_password)
            }
    }

    private fun storeNewPassword(
        email: String,
        oldPassword: String,
        newPassword: String,
        callback: UpdateProfileCallback
    ) {
        val credential = EmailAuthProvider.getCredential(email, oldPassword)
        auth.currentUser?.reauthenticate(credential)
            ?.addOnCompleteListener {
                auth.currentUser?.updatePassword(newPassword)!!
                    .addOnSuccessListener {
                        callback.onSuccess()
                    }
                    .addOnFailureListener {
                        callback.onFailure(R.string.txt_failure_update)
                    }
            }
    }

    fun resetPassword(email: String, callback: ResetPasswordCallback) {
        database.orderByChild("email").equalTo(email).limitToFirst(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        sendResetPasswordEmail(email, callback)
                    } else {
                        callback.onFailure(R.string.txt_email_not_found)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun sendResetPasswordEmail(email: String, callback: ResetPasswordCallback) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                callback.onSuccess()
            }
            .addOnFailureListener {
                callback.onFailure(R.string.txt_email_not_found)
            }
    }
}