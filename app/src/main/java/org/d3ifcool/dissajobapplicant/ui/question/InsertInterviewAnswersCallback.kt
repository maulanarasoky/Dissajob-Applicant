package org.d3ifcool.dissajobapplicant.ui.question

interface InsertInterviewAnswersCallback {
    fun onSuccessAdding()
    fun onFailureAdding(messageId: Int)
}