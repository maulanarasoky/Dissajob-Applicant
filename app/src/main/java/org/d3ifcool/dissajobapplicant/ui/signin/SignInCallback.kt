package org.d3ifcool.dissajobapplicant.ui.signin

interface SignInCallback {
    fun onSuccess()
    fun onNotVerified()
    fun onFailure()
}