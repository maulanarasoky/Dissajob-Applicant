package org.d3ifcool.dissajobapplicant.ui.auth

import androidx.lifecycle.ViewModel

class AuthViewModel: ViewModel() {
    val authState = FirebaseUserLiveData()
}