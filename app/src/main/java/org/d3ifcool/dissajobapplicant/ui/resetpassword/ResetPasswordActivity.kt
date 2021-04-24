package org.d3ifcool.dissajobapplicant.ui.resetpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivityResetPasswordBinding
import org.d3ifcool.dissajobapplicant.ui.profile.ApplicantViewModel
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import java.util.regex.Pattern

class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener, ResetPasswordCallback {

    private lateinit var activityResetPasswordBinding: ActivityResetPasswordBinding

    private lateinit var viewModel: ApplicantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResetPasswordBinding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(activityResetPasswordBinding.root)

        activityResetPasswordBinding.toolbar.title =
            resources.getString(R.string.txt_back)
        setSupportActionBar(activityResetPasswordBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]

        activityResetPasswordBinding.btnResetPassword.setOnClickListener(this)
    }

    private fun formValidation() {
        val email = activityResetPasswordBinding.etEmail.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            etEmail.error = resources.getString(R.string.edit_text_error_alert, "Email")
            return
        }

        if (!isValidMail(email)) {
            etEmail.error = resources.getString(R.string.edit_text_alert_email_invalid)
            return
        }

        viewModel.resetPassword(email, this)
    }

    private fun isValidMail(email: String): Boolean {
        val emailString = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        return Pattern.compile(emailString).matcher(email).matches()
    }

    override fun onSuccess() {
        Toast.makeText(
            this,
            resources.getString(R.string.txt_success_alert_reset_password),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    override fun onFailure(messageId: Int) {
        Toast.makeText(this, resources.getString(messageId), Toast.LENGTH_SHORT).show()
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
            R.id.btnResetPassword -> formValidation()
        }
    }
}