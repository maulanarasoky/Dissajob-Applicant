package org.d3ifcool.dissajobapplicant.ui.profile.callback

interface UploadFileCallback {
    fun onSuccessUpload(fileId: String)
    fun onFailureUpload(messageId: Int)
}