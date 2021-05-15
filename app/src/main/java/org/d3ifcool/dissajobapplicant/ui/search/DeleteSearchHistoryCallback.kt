package org.d3ifcool.dissajobapplicant.ui.search

interface DeleteSearchHistoryCallback {
    fun onSuccess()
    fun onFailure(messageId: Int)
}