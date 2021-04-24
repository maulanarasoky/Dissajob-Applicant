package org.d3ifcool.dissajobapplicant.ui.job.callback

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.SavedJobResponseEntity

interface LoadSavedJobsCallback {
    fun onAllJobsReceived(jobResponse: List<SavedJobResponseEntity>): List<SavedJobResponseEntity>
}