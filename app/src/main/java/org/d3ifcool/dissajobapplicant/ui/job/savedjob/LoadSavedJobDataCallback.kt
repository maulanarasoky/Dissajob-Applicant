package org.d3ifcool.dissajobapplicant.ui.job.savedjob

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.SavedJobResponseEntity

interface LoadSavedJobDataCallback {
    fun onSavedJobDataCallback(savedJobResponse: SavedJobResponseEntity): SavedJobResponseEntity
}