package org.d3ifcool.dissajobapplicant.utils.database

import android.net.Uri
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity
import org.d3ifcool.dissajobapplicant.ui.cv.callback.AddCvCallback
import org.d3ifcool.dissajobapplicant.ui.cv.callback.LoadCvCallback
import org.d3ifcool.dissajobapplicant.ui.cv.callback.LoadCvFileCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import java.io.File


object CvHelper {

    private val storageRef = Firebase.storage.reference
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
                            val cvData = CvResponseEntity(
                                data.key.toString(),
                                data.child("cvName").value.toString(),
                                data.child("cvDescription").value.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("fileId").value.toString()
                            )
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

    fun getCvById(cvId: String, callback: LoadCvFileCallback) {
//        storageRef.child("applicant/profile/cv/$cvId").downloadUrl
//            .addOnSuccessListener {
//
//            }

//        val localFile = File.createTempFile("application", "pdf")
//        storageRef.child("applicant/profile/cv/$cvId").getFile(localFile).addOnSuccessListener {
//            callback.onCvFileReceived(it.storage.stream.result.stream)
//        }

        storageRef.child("applicant/profile/cv/$cvId").getBytes(Long.MAX_VALUE).addOnSuccessListener {
            callback.onCvFileReceived(it)
        }
    }
}