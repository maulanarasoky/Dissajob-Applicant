package org.d3ifcool.dissajobapplicant.ui.cv

interface AddCvCallback {
    fun onSuccessAdding()
    fun onFailureAdding(messageId: Int)
}