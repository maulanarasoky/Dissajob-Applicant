package org.d3ifcool.dissajobapplicant.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivityChangePasswordBinding
import org.d3ifcool.dissajobapplicant.ui.profile.ApplicantViewModel
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UpdateProfileCallback
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.database.AuthHelper
import java.util.regex.Pattern

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener, UpdateProfileCallback {

    private lateinit var activityChangePasswordBinding: ActivityChangePasswordBinding

    private lateinit var viewModel: ApplicantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(activityChangePasswordBinding.root)

        activityChangePasswordBinding.toolbar.title =
            resources.getString(R.string.txt_change_password_title)
        setSupportActionBar(activityChangePasswordBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        textWatcher()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]

        activityChangePasswordBinding.btnUpdate.setOnClickListener(this)
    }

    private fun formValidation() {
        val oldPassword = activityChangePasswordBinding.etOldPassword.text.toString().trim()
        val newPassword = activityChangePasswordBinding.etNewPassword.text.toString().trim()
        val confirmPassword = activityChangePasswordBinding.etConfirmPassword.text.toString().trim()
        if (TextUtils.isEmpty(oldPassword)) {
            activityChangePasswordBinding.etOldPassword.error =
                getString(R.string.edit_text_error_alert, "Password lama")
            return
        }

        if (TextUtils.isEmpty(newPassword)) {
            activityChangePasswordBinding.etNewPassword.error =
                getString(R.string.edit_text_error_alert, "Password baru")
            return
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            activityChangePasswordBinding.etConfirmPassword.error =
                getString(R.string.edit_text_error_alert, "Re-type password")
            return
        }

        if (newPassword != confirmPassword) {
            activityChangePasswordBinding.etNewPassword.error =
                getString(R.string.edit_text_alert_password_not_match)
            activityChangePasswordBinding.etConfirmPassword.error =
                getString(R.string.edit_text_alert_password_not_match)
            return
        }

        updatePassword(oldPassword, newPassword)
    }

    private fun updatePassword(oldPassword: String, newPassword: String) {
        isEnable(false)
        val email = AuthHelper.currentUser?.email.toString()
        viewModel.updateApplicantPassword(email, oldPassword, newPassword, this)
    }

    private fun isEnable(state: Boolean) {
        activityChangePasswordBinding.etOldPassword.isEnabled = state
        activityChangePasswordBinding.etNewPassword.isEnabled = state
        activityChangePasswordBinding.etConfirmPassword.isEnabled = state
        activityChangePasswordBinding.btnUpdate.isEnabled = state
    }

    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!?$%^&+=])(?=\\S+$).{8,}$"
        return Pattern.compile(regex).matcher(password).matches()
    }

    private fun textWatcher() {
        activityChangePasswordBinding.etNewPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (activityChangePasswordBinding.etNewPassword.text.trim().length >= 8) {
                    activityChangePasswordBinding.imgPasswordFirstRule.setImageResource(R.drawable.ic_check_circle_color_primary_24dp)
                } else {
                    activityChangePasswordBinding.imgPasswordFirstRule.setImageResource(R.drawable.ic_check_circle_gray_24dp)
                }

                if (isValidPassword(activityChangePasswordBinding.etNewPassword.text.toString().trim())) {
                    activityChangePasswordBinding.imgPasswordSecondRule.setImageResource(R.drawable.ic_check_circle_color_primary_24dp)
                    Log.d("SUCCESS", "SUCCESS RULE SECOND")
                } else {
                    activityChangePasswordBinding.imgPasswordSecondRule.setImageResource(R.drawable.ic_check_circle_gray_24dp)
                }
            }

        })
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
            R.id.btnUpdate -> {
                formValidation()
            }
        }
    }

    override fun onSuccess() {
        Toast.makeText(
            this,
            resources.getString(R.string.txt_success_update, "Password"),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    override fun onFailure(messageId: Int) {
        Toast.makeText(this, resources.getString(messageId, "Email"), Toast.LENGTH_SHORT).show()
        isEnable(true)
    }
}