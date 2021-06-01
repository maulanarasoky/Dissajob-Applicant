package org.d3ifcool.dissajobapplicant.ui.experience.callback

interface AddExperienceCallback {
    fun onSuccessAdding()
    fun onFailureAdding(messageId: Int)
}