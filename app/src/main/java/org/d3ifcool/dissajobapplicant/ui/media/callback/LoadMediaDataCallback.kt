package org.d3ifcool.dissajobapplicant.ui.media.callback

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.media.MediaResponseEntity

interface LoadMediaDataCallback {
    fun onAllMediaReceived(mediaResponse: List<MediaResponseEntity>): List<MediaResponseEntity>
}