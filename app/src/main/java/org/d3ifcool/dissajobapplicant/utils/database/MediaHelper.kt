package org.d3ifcool.dissajobapplicant.utils.database

import android.net.Uri
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.media.MediaResponseEntity
import org.d3ifcool.dissajobapplicant.ui.media.callback.AddMediaCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.LoadMediaDataCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.LoadMediaFileCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.UpdateMediaCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback


object MediaHelper {

    private val storageRef = Firebase.storage.reference
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("media")
    private val ARR_MEDIA: MutableList<MediaResponseEntity> = mutableListOf()

    fun getMediaId(applicantId: String, callback: LoadMediaDataCallback) {
        database.orderByChild("applicantId").equalTo(applicantId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    ARR_MEDIA.clear()
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children.reversed()) {
                            val mediaData = MediaResponseEntity(
                                data.key.toString(),
                                data.child("mediaName").value.toString(),
                                data.child("mediaDescription").value.toString(),
                                data.child("applicantId").value.toString(),
                                data.child("fileId").value.toString()
                            )
                            ARR_MEDIA.add(mediaData)
                        }
                    }
                    callback.onAllMediaReceived(ARR_MEDIA)
                }
            })
    }

    fun uploadMedia(media: Uri, callback: UploadFileCallback) {
        val storageRef = Firebase.storage.reference
        val fileId = database.push().key
        val uploadImage = storageRef.child("applicant/media/${fileId}").putFile(media)
        uploadImage.addOnSuccessListener {
            callback.onSuccessUpload(fileId.toString())
        }.addOnFailureListener {
            callback.onFailureUpload(R.string.txt_failure_upload_media)
        }
    }

    fun addMedia(mediaData: MediaResponseEntity, callback: AddMediaCallback) {
        val id = database.push().key
        mediaData.id = id.toString()
        mediaData.applicantId = AuthHelper.currentUser?.uid.toString()
        database.child(mediaData.id).setValue(mediaData).addOnSuccessListener {
            callback.onSuccessAdding()
        }.addOnFailureListener {
            callback.onFailureAdding(R.string.txt_failure_update)
        }
    }

    fun updateMedia(mediaData: MediaResponseEntity, callback: UpdateMediaCallback) {
        database.child(mediaData.id).setValue(mediaData).addOnSuccessListener {
            callback.onSuccessUpdate()
        }.addOnFailureListener {
            callback.onFailureUpdate(R.string.txt_failure_update)
        }
    }

    fun getMediaById(fileId: String, callback: LoadMediaFileCallback) {
        storageRef.child("applicant/media/$fileId").getBytes(Long.MAX_VALUE)
            .addOnSuccessListener {
                callback.onMediaFileReceived(it)
            }
    }
}