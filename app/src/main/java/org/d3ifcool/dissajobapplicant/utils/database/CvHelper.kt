package org.d3ifcool.dissajobapplicant.utils.database

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity
import org.d3ifcool.dissajobapplicant.ui.cv.AddCvCallback
import org.d3ifcool.dissajobapplicant.ui.cv.LoadCvCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback

object CvHelper {

    private val auth = FirebaseAuth.getInstance()
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("cv")
    private val arrCv: MutableList<CvResponseEntity> = mutableListOf()

    fun getCvId(applicantId: String, callback: LoadCvCallback) {
        database.orderByChild("applicantId").equalTo(applicantId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arrCv.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val cvData =
                                data.getValue<CvResponseEntity>() ?: return
                            cvData.id = data.key ?: return

                            arrCv.add(cvData)
                        }
                    }
                    callback.onAllCvReceived(arrCv)
                }
            })
    }

    fun uploadCv(cv: Uri, callback: UploadFileCallback) {
        val storageRef = Firebase.storage.reference
        val fileId = database.push().key
        val uploadImage = storageRef.child("applicant/profile/cv/${fileId}").putFile(cv)
        uploadImage.addOnSuccessListener {
            callback.onSuccessUpload(fileId.toString())
        }.addOnFailureListener {
            callback.onFailureUpload(R.string.txt_failure_upload_cv)
        }
    }

    fun addCv(cvData: CvResponseEntity, callbackTo: AddCvCallback) {
        val id = database.push().key
        cvData.id = id.toString()
        cvData.applicantId = AuthHelper.currentUser?.uid.toString()
        database.child(cvData.id.toString()).setValue(cvData).addOnSuccessListener {
            callbackTo.onSuccessAdding()
        }.addOnFailureListener {
            callbackTo.onFailureAdding(R.string.txt_failure_update)
        }
    }
}