package org.d3ifcool.dissajobapplicant.ui.media.callback

interface LoadPdfCallback {
    fun onLoadPdfData(mediaId: String, callback: LoadPdfCallback)
    fun onPdfDataReceived(mediaFile: ByteArray)
}