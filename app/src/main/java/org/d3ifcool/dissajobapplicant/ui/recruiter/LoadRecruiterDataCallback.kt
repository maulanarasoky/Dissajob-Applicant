package org.d3ifcool.dissajobapplicant.ui.recruiter

import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity

interface LoadRecruiterDataCallback {
    fun onLoadRecruiterData(recruiterId: String, callback: LoadRecruiterDataCallback)
    fun onRecruiterDataReceived(recruiterData: RecruiterEntity)
}