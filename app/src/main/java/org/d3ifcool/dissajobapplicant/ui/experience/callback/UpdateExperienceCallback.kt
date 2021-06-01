package org.d3ifcool.dissajobapplicant.ui.experience.callback

interface UpdateExperienceCallback {
    fun onSuccessUpdate()
    fun onFailureUpdate(messageId: Int)
}