package org.d3ifcool.dissajobapplicant.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivitySignUpBinding
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity(), View.OnClickListener, SignUpCallback {

    private lateinit var activitySignUpBinding: ActivitySignUpBinding

    private lateinit var viewModel: SignUpViewModel

    private lateinit var dialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(activitySignUpBinding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[SignUpViewModel::class.java]

        activitySignUpBinding.btnSignUp.setOnClickListener(this)
        activitySignUpBinding.footer.btnSignIn.setOnClickListener(this)
    }

    private fun signUp() {
        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_loading)
        dialog.setCancelable(false)
        dialog.show()


        val firstName = activitySignUpBinding.etFirstName.text.toString().trim()
        val lastName = activitySignUpBinding.etLastName.text.toString().trim()
        val fullName = "$firstName $lastName"
        val email = activitySignUpBinding.etEmail.text.toString().trim()
        val phoneNumber = activitySignUpBinding.etPhoneNumber.text.toString().trim()
        val password = activitySignUpBinding.etPassword.text.toString().trim()
        val user = ApplicantResponseEntity(
            id = "",
            firstName = firstName,
            lastName = lastName,
            fullName = fullName,
            email = email,
            phoneNumber = phoneNumber
        )

        viewModel.signUp(email, password, user, this)
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activitySignUpBinding.etFirstName.text.toString().trim())) {
            activitySignUpBinding.etFirstName.error =
                resources.getString(R.string.edit_text_error_alert, "Nama depan")
            return
        }

        if (TextUtils.isEmpty(activitySignUpBinding.etEmail.text.toString().trim())) {
            activitySignUpBinding.etEmail.error = resources.getString(R.string.edit_text_error_alert, "Email")
            return
        }

        if (!isValidMail(activitySignUpBinding.etEmail.text.toString().trim())) {
            activitySignUpBinding.etEmail.error = resources.getString(R.string.edit_text_alert_email_invalid)
            return
        }

        if (TextUtils.isEmpty(activitySignUpBinding.etPhoneNumber.text.toString().trim())) {
            activitySignUpBinding.etPhoneNumber.error = resources.getString(R.string.edit_text_error_alert, "Nomor telepon")
            return
        }

        if (!isValidPhoneNumber(activitySignUpBinding.etPhoneNumber.text.toString().trim())) {
            activitySignUpBinding.etPhoneNumber.error = resources.getString(R.string.edit_text_alert_phone_number_invalid)
            return
        }

        if (TextUtils.isEmpty(activitySignUpBinding.etPassword.text.toString().trim())) {
            activitySignUpBinding.etPassword.error =
                resources.getString(R.string.edit_text_error_alert, "Password")
            return
        }

        if (TextUtils.isEmpty(activitySignUpBinding.etConfirmPassword.text.toString().trim())) {
            activitySignUpBinding.etConfirmPassword.error =
                resources.getString(R.string.edit_text_error_alert, "Confirm password")
            return
        }

        if (activitySignUpBinding.etPassword.text.toString() != activitySignUpBinding.etConfirmPassword.text.toString()) {
            activitySignUpBinding.etPassword.error =
                resources.getString(R.string.edit_text_alert_password_not_match, "Password dan confirm password")
            activitySignUpBinding.etConfirmPassword.error =
                resources.getString(R.string.edit_text_alert_password_not_match, "Password dan confirm password")
            return
        }

        signUp()
    }

    private fun isValidMail(email: String): Boolean {
        val emailString = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        return Pattern.compile(emailString).matcher(email).matches()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length in 7..13
        } else false
    }

    override fun onSuccess() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_alert_success_sign_up)
        dialog.setConfirmClickListener {
            this.finish()
        }
        dialog.show()
    }

    override fun onFailure(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSignIn -> finish()
            R.id.btnSignUp -> formValidation()
        }
    }
}