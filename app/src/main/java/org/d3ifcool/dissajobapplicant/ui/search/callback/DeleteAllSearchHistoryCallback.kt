package org.d3ifcool.dissajobapplicant.ui.search.callback

interface DeleteAllSearchHistoryCallback {
    fun onSuccessDeleteAllHistory()
    fun onFailureDeleteAllHistory(messageId: Int)
}