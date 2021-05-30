package org.d3ifcool.dissajobapplicant.ui.media.callback

interface AddMediaCallback {
    fun onSuccessAdding()
    fun onFailureAdding(messageId: Int)
}