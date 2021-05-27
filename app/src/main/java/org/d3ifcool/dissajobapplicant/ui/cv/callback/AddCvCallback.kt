package org.d3ifcool.dissajobapplicant.ui.cv.callback

interface AddCvCallback {
    fun onSuccessAdding()
    fun onFailureAdding(messageId: Int)
}