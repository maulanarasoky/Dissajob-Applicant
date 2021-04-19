package org.d3ifcool.dissajobapplicant.ui.profile.callback

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity

interface LoadApplicantDetailsCallback {
    fun onApplicantDetailsReceived(applicantResponse: ApplicantResponseEntity): ApplicantResponseEntity
}