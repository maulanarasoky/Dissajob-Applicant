package org.d3ifcool.dissajobapplicant.ui.job.callback

interface UnSaveJobCallback {
    fun onSuccessUnSave()
    fun onFailureUnSave(messageId: Int)
}