package org.d3ifcool.dissajobapplicant.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.recruiter.RecruiterResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteRecruiterSource

object RecruiterHelper {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users").child("recruiters")

    fun getRecruiterData(userId: String, callback: RemoteRecruiterSource.LoadRecruiterDataCallback) {
        database.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val recruiter = RecruiterResponseEntity(
                        dataSnapshot.key.toString(),
                        dataSnapshot.child("firstName").value.toString(),
                        dataSnapshot.child("lastName").value.toString(),
                        dataSnapshot.child("fullName").value.toString(),
                        dataSnapshot.child("email").value.toString(),
                        dataSnapshot.child("address").value.toString(),
                        dataSnapshot.child("phoneNumber").value.toString(),
                        dataSnapshot.child("role").value.toString(),
                        dataSnapshot.child("imagePath").value.toString()
                    )
                    callback.onRecruiterDataReceived(recruiter)
                }
            }

        })
    }
}