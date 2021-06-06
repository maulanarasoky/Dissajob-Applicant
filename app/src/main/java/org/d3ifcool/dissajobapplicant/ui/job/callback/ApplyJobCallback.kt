package org.d3ifcool.dissajobapplicant.ui.job.callback

interface ApplyJobCallback {
    fun onSuccessApply()
    fun onFailureApply(messageId: Int)
}