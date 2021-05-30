package org.d3ifcool.dissajobapplicant.ui.media.callback

interface UpdateMediaCallback {
    fun onSuccessUpdate()
    fun onFailureUpdate(messageId: Int)
}