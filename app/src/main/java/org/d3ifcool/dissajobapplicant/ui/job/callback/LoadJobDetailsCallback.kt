package org.d3ifcool.dissajobapplicant.ui.job.callback

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.JobDetailsResponseEntity

interface LoadJobDetailsCallback {
    fun onJobDetailsReceived(jobResponse: JobDetailsResponseEntity): JobDetailsResponseEntity
}