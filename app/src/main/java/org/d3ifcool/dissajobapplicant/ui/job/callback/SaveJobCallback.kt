package org.d3ifcool.dissajobapplicant.ui.job.callback

interface SaveJobCallback {
    fun onSuccess()
    fun onFailure(messageId: Int)
}