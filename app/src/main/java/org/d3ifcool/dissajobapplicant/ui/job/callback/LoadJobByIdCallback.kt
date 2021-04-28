package org.d3ifcool.dissajobapplicant.ui.job.callback

import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity

interface LoadJobByIdCallback {
    fun onLoadJobData(jobId: String, callback: LoadJobByIdCallback)
    fun onJobReceived(jobEntity: JobEntity)
}