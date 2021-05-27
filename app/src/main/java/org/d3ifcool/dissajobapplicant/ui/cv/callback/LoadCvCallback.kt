package org.d3ifcool.dissajobapplicant.ui.cv.callback

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity

interface LoadCvCallback {
    fun onAllCvReceived(cvResponse: List<CvResponseEntity>): List<CvResponseEntity>
}