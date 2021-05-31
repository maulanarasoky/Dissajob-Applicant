package org.d3ifcool.dissajobapplicant.ui.education.callback

interface UpdateEducationCallback {
    fun onSuccessUpdate()
    fun onFailureUpdate(messageId: Int)
}