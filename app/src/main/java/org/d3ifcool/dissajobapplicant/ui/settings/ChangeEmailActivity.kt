package org.d3ifcool.dissajobapplicant.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivityChangeEmailBinding
import org.d3ifcool.dissajobapplicant.ui.profile.ApplicantViewModel
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UpdateProfileCallback
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.AuthHelper
import java.util.regex.Pattern

class ChangeEmailActivity : AppCompatActivity(), UpdateProfileCallback, View.OnClickListener {

    private lateinit var activityChangeEmailBinding: ActivityChangeEmailBinding

    private lateinit var viewModel: ApplicantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangeEmailBinding = ActivityChangeEmailBinding.inflate(layoutInflater)
        setContentView(activityChangeEmailBinding.root)

        activityChangeEmailBinding.toolbar.title =
            resources.getString(R.string.txt_change_email_title)
        setSupportActionBar(activityChangeEmailBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        showCurrentEmail()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]

        activityChangeEmailBinding.btnUpdate.setOnClickListener(this)
    }

    private fun showCurrentEmail() {
        activityChangeEmailBinding.etOldEmail.setText(AuthHelper.currentUser?.email.toString())
    }

    private fun formValidation() {
        val newEmail = activityChangeEmailBinding.etNewEmail.text.toString().trim()
        val password = activityChangeEmailBinding.etPassword.text.toString().trim()

        if (TextUtils.isEmpty(newEmail)) {
            activityChangeEmailBinding.etNewEmail.error =
                getString(R.string.edit_text_error_alert, "Email")
            return
        }

        if (!TextUtils.isEmpty(newEmail)) {
            if (!isValidMail(newEmail)) {
                activityChangeEmailBinding.etNewEmail.error =
                    getString(R.string.edit_text_alert_email_invalid)
                return
            }

            if (newEmail == AuthHelper.currentUser?.email.toString()) {
                activityChangeEmailBinding.etNewEmail.error =
                    getString(R.string.txt_same_email)
                return
            }
        }

        if (TextUtils.isEmpty(password)) {
            activityChangeEmailBinding.etPassword.error =
                getString(R.string.edit_text_error_alert, "Password")
            return
        }

        storeToDatabase(newEmail, password)
    }

    private fun storeToDatabase(newEmail: String, password: String) {
        isEnable(false)
        viewModel.updateApplicantEmail(
            AuthHelper.currentUser?.uid.toString(),
            newEmail,
            password,
            this
        )
    }

    private fun isValidMail(email: String): Boolean {
        val emailString = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        return Pattern.compile(emailString).matcher(email).matches()
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

    override fun onSuccess() {
        Toast.makeText(
            this,
            resources.getString(R.string.txt_success_update, "Email"),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    override fun onFailure(messageId: Int) {
        Toast.makeText(this, resources.getString(messageId, "Email"), Toast.LENGTH_SHORT).show()
        isEnable(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnUpdate -> formValidation()
        }
    }

    private fun isEnable(state: Boolean) {
        activityChangeEmailBinding.etNewEmail.isEnabled = state
        activityChangeEmailBinding.etPassword.isEnabled = state
        activityChangeEmailBinding.btnUpdate.isEnabled = state
    }
}