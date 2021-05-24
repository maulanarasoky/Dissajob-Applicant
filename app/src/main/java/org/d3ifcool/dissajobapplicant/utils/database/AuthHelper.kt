package org.d3ifcool.dissajobapplicant.utils.database

import com.google.firebase.auth.FirebaseAuth

object AuthHelper {
    val currentUser = FirebaseAuth.getInstance().currentUser
}