package org.d3ifcool.dissajobapplicant.ui.experience

interface AddExperienceCallback {
    fun onSuccessAdding()
    fun onFailureAdding(messageId: Int)
}