package org.d3ifcool.dissajobapplicant.ui.education

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education.EducationResponseEntity

interface LoadEducationsCallback {
    fun onAllEducationsReceived(educationResponse: List<EducationResponseEntity>): List<EducationResponseEntity>
}