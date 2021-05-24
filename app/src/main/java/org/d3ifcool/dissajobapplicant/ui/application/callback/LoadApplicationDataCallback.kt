package org.d3ifcool.dissajobapplicant.ui.application.callback

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.application.ApplicationResponseEntity

interface LoadApplicationDataCallback {
    fun onApplicationDataReceived(applicationResponse: ApplicationResponseEntity): ApplicationResponseEntity
}