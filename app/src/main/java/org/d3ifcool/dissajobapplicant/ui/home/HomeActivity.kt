package org.d3ifcool.dissajobapplicant.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivityHomeBinding
import org.d3ifcool.dissajobapplicant.ui.auth.AuthViewModel
import org.d3ifcool.dissajobapplicant.ui.notification.NotificationFragment
import org.d3ifcool.dissajobapplicant.ui.profile.ProfileFragment
import org.d3ifcool.dissajobapplicant.ui.signin.SignInActivity

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val STATE_RESULT = "state_result"
    }

    private lateinit var activityHomeBinding: ActivityHomeBinding

    private lateinit var mUserObserver: Observer<FirebaseUser?>

    private var bottomNavState = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)

        checkLogin()

        val viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        mUserObserver = Observer { updateUI(savedInstanceState) }
        viewModel.authState.observe(this, mUserObserver)
    }

    private fun updateUI(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            bottomNavState = savedInstanceState.getInt(STATE_RESULT, 0)
        }

        activityHomeBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            if (bottomNavState != item.itemId) {
                bottomNavState = item.itemId
                when (item.itemId) {
                    R.id.home -> loadHomeFragment()
                    R.id.notification -> loadNotificationFragment()
                    R.id.profile -> loadProfileFragment()
                }
            }
            true
        }
        if (savedInstanceState == null && bottomNavState == 0) {
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
    private fun loadNotificationFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.container_layout,
                NotificationFragment(),
                NotificationFragment::class.java.simpleName
            )
            .commit()
    }

    private fun loadProfileFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.container_layout,
                ProfileFragment(),
                ProfileFragment::class.java.simpleName
            )
            .commit()
    }

    private fun checkLogin() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            this.finish()
            return
        }
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_RESULT, bottomNavState)
    }
}