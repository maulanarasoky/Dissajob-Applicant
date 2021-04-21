package org.d3ifcool.dissajobapplicant.ui.profile.callback

interface UploadProfilePictureCallback {
    fun onSuccessUpload(imageId: String)
    fun onFailureUpload(messageId: Int)
}