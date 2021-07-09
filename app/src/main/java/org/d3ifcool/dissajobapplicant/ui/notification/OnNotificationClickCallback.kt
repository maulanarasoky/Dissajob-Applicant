package org.d3ifcool.dissajobapplicant.ui.notification

interface OnNotificationClickCallback {
    fun onItemClick(applicationId: String, jobId: String, recruiterId: String)
}