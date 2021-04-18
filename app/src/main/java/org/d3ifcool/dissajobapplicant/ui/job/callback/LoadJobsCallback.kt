package org.d3ifcool.dissajobapplicant.ui.job.callback

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.JobResponseEntity

interface LoadJobsCallback {
    fun onAllJobsReceived(jobResponse: List<JobResponseEntity>): List<JobResponseEntity>
}