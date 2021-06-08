package org.d3ifcool.dissajobapplicant.ui.job.callback

interface ApplyJobCallback {
    fun onSuccessApply(applicationId: String)
    fun onFailureApply(messageId: Int)
}