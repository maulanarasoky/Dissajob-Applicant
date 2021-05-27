package org.d3ifcool.dissajobapplicant.ui.cv.callback

interface LoadCvFileCallback {
    fun onCvFileReceived(cvFile: ByteArray)
}