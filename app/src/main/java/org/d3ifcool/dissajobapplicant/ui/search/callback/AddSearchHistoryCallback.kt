package org.d3ifcool.dissajobapplicant.ui.search.callback

interface AddSearchHistoryCallback {
    fun onSuccessAdding()
    fun onFailureAdding(messageId: Int)
}