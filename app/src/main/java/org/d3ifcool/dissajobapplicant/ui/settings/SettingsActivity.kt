package org.d3ifcool.dissajobapplicant.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivitySettingsBinding
import org.d3ifcool.dissajobapplicant.ui.profile.ApplicantViewModel
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.SignOutDialog
import org.d3ifcool.dissajobapplicant.vo.Status

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activitySettingsBinding: ActivitySettingsBinding

    private lateinit var viewModel: ApplicantViewModel

    private val applicantId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)

        activitySettingsBinding.toolbar.title = resources.getString(R.string.txt_settings)
        setSupportActionBar(activitySettingsBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
        showCurrentProfileData()

        //Change profile section
        activitySettingsBinding.profileSection.btnChangeProfile.setOnClickListener(this)
        activitySettingsBinding.profileSection.btnChangePhoneNumber.setOnClickListener(this)
        activitySettingsBinding.profileSection.btnChangeEmail.setOnClickListener(this)

        //Change password section
        activitySettingsBinding.securitySection.btnChangePassword.setOnClickListener(this)

        //Advance section
        activitySettingsBinding.advanceSection.btnSignOut.setOnClickListener(this)
    }

    private fun showCurrentProfileData() {
        viewModel.getApplicantDetails(applicantId)
            .observe(this) { profileData ->
                if (profileData.data != null) {
                    when (profileData.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            activitySettingsBinding.profileSection.tvPhoneNumber.text =
                                profileData.data.phoneNumber.toString()
                            activitySettingsBinding.profileSection.tvEmail.text =
                                profileData.data.email.toString()
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnChangeProfile -> {
                startActivity(Intent(this, ChangeProfileActivity::class.java))
            }
            R.id.btnChangePhoneNumber -> {
                startActivity(Intent(this, ChangePhoneNumberActivity::class.java))
            }
            R.id.btnChangeEmail -> {
                startActivity(Intent(this, ChangeEmailActivity::class.java))
            }
            R.id.btnChangePassword -> {
                startActivity(Intent(this, ChangePasswordActivity::class.java))
            }
            R.id.btnSignOut -> {
                SignOutDialog().show(supportFragmentManager, SignOutDialog::class.java.simpleName)
            }
        }
    }
}