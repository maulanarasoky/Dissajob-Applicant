package org.d3ifcool.dissajobapplicant.utils

interface InsertToDatabaseCallback {
    fun onSuccess()
    fun onFailure(messageId: Int)
}