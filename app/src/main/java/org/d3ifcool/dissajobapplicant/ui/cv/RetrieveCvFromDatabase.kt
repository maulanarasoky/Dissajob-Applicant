package org.d3ifcool.dissajobapplicant.ui.cv

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity

interface RetrieveCvFromDatabase {
    fun onAllCvReceived(cvResponse: List<CvResponseEntity>): List<CvResponseEntity>
}