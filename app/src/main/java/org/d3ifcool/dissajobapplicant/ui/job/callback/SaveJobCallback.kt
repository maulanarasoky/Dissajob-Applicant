package org.d3ifcool.dissajobapplicant.ui.job.callback

interface SaveJobCallback {
    fun onSuccessSave(saveJobId: String)
    fun onFailureSave(messageId: Int)
}