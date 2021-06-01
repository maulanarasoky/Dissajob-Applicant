package org.d3ifcool.dissajobapplicant.ui.experience

interface UpdateExperienceCallback {
    fun onSuccessUpdate()
    fun onFailureUpdate(messageId: Int)
}