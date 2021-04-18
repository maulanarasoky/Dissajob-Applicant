package org.d3ifcool.dissajobapplicant.utils

import com.google.firebase.auth.FirebaseAuth

object AuthHelper {
    val currentUser = FirebaseAuth.getInstance().currentUser
}