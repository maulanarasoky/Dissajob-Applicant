package org.d3ifcool.dissajobapplicant.ui.signup

interface SignUpCallback {
    fun onSuccess()
    fun onFailure(message: String)
}