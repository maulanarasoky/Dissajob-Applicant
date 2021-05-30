package org.d3ifcool.dissajobapplicant.ui.media.callback

interface LoadMediaFileCallback {
    fun onMediaFileReceived(mediaFile: ByteArray): ByteArray
}