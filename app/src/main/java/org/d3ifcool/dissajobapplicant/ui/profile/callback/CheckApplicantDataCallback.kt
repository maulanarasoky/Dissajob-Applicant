package org.d3ifcool.dissajobapplicant.ui.profile.callback

interface CheckApplicantDataCallback {
    fun allDataAvailable()
    fun profileDataNotAvailable()
    fun phoneNumberNotAvailable()
}