package org.d3ifcool.dissajobapplicant.ui.cv.callback

interface LoadPdfCallback {
    fun onLoadPdfData(cvId: String, callback: LoadPdfCallback)
    fun onPdfDataReceived(cvFile: ByteArray)
}