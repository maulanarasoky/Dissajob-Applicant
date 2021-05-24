package org.d3ifcool.dissajobapplicant.ui.search.callback

interface DeleteSearchHistoryCallback {
    fun onSuccessDelete()
    fun onFailureDelete(messageId: Int)
}