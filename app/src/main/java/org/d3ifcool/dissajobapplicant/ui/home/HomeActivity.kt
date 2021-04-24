package org.d3ifcool.dissajobapplicant.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivityHomeBinding
import org.d3ifcool.dissajobapplicant.ui.auth.AuthViewModel
import org.d3ifcool.dissajobapplicant.ui.signin.SignInActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var activityHomeBinding: ActivityHomeBinding

    private var bottomNavState = 0

    private lateinit var mUserObserver: Observer<FirebaseUser?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            this.finish()
            return
        }

        val viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        mUserObserver = Observer { updateUI(savedInstanceState) }
        viewModel.authState.observe(this, mUserObserver)
    }

    private fun updateUI(savedInstanceState: Bundle?) {
        activityHomeBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            if (bottomNavState != item.itemId) {
                bottomNavState = item.itemId
                when (item.itemId) {
                    R.id.home -> {
                        loadHomeFragment()
                    }
                    R.id.notification -> {
                    }
                    R.id.profile -> {
                    }
                }
            }
            true
        }
        if (savedInstanceState == null || bottomNavState == 0) {
            activityHomeBinding.bottomNavigation.selectedItemId = R.id.home
        }
    }

    private fun loadHomeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_layout, HomeFragment(), HomeFragment::class.java.simpleName)
            .commit()
    }
//
//    private fun loadNotificationFragment() {
//        supportFragmentManager
//            .beginTransaction()
//            .replace(
//                R.id.container_layout,
//                NotificationFragment(),
//                NotificationFragment::class.java.simpleName
//            )
//            .commit()
//    }
//
//    private fun loadProfileFragment() {
//        supportFragmentManager
//            .beginTransaction()
//            .replace(
//                R.id.container_layout,
//                ProfileFragment(),
//                ProfileFragment::class.java.simpleName
//            )
//            .commit()
//    }
}