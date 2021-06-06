package org.d3ifcool.dissajobapplicant.ui.job.callback

interface SaveJobCallback {
    fun onSuccessSave()
    fun onFailureSave(messageId: Int)
}