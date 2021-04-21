package org.d3ifcool.dissajobapplicant.ui.resetpassword

interface ResetPasswordCallback {
    fun onSuccess()
    fun onFailure(messageId: Int)
}