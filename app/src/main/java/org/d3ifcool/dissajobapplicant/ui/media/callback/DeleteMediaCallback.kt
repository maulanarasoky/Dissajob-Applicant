package org.d3ifcool.dissajobapplicant.ui.media.callback

interface DeleteMediaCallback {
    fun onSuccessDelete()
    fun onFailureDelete(messageId: Int)
}