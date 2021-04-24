package org.d3ifcool.dissajobapplicant.ui.job.callback

interface ApplyJobCallback {
    fun onSuccess()
    fun onFailure(messageId: Int)
}