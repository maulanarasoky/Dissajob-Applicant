package org.d3ifcool.dissajobapplicant.ui.experience.callback

interface DeleteExperienceCallback {
    fun onSuccessDelete()
    fun onFailureDelete(messageId: Int)
}