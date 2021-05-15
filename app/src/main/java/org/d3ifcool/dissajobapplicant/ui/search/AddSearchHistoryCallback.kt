package org.d3ifcool.dissajobapplicant.ui.search

interface AddSearchHistoryCallback {
    fun onSuccess()
    fun onFailure(messageId: Int)
}