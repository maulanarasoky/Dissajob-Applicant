package org.d3ifcool.dissajobapplicant.ui.profile.callback

interface UpdateProfileCallback {
    fun onSuccess()
    fun onFailure(messageId: Int)
}