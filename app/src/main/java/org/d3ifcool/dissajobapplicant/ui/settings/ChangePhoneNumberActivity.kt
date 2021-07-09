package org.d3ifcool.dissajobapplicant.ui.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivityChangePhoneNumberBinding
import org.d3ifcool.dissajobapplicant.ui.profile.ApplicantViewModel
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UpdateProfileCallback
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.vo.Status
import java.util.regex.Pattern

class ChangePhoneNumberActivity : AppCompatActivity(), View.OnClickListener, UpdateProfileCallback {

    private lateinit var activityChangePhoneNumberBinding: ActivityChangePhoneNumberBinding

    private lateinit var viewModel: ApplicantViewModel

    private lateinit var dialog: SweetAlertDialog

    private val applicantId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangePhoneNumberBinding = ActivityChangePhoneNumberBinding.inflate(layoutInflater)
        setContentView(activityChangePhoneNumberBinding.root)

        activityChangePhoneNumberBinding.toolbar.title =
            resources.getString(R.string.txt_change_phone_number_title)
        setSupportActionBar(activityChangePhoneNumberBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]

        showCurrentPhoneNumber()

        activityChangePhoneNumberBinding.btnUpdate.setOnClickListener(this)
    }

    private fun showCurrentPhoneNumber() {
        viewModel.getApplicantDetails(applicantId)
            .observe(this@ChangePhoneNumberActivity) { profileData ->
                if (profileData.data != null) {
                    when (profileData.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            if (profileData.data.phoneNumber != "-") {
                                activityChangePhoneNumberBinding.textInputOldPhoneNumber.visibility =
                                    View.VISIBLE
                                activityChangePhoneNumberBinding.etOldPhoneNumber.visibility =
                                    View.VISIBLE
                                activityChangePhoneNumberBinding.etOldPhoneNumber.setText(
                                    profileData.data.phoneNumber.toString()
                                )
                            }
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    private fun formValidation() {
        val newPhoneNumber =
            activityChangePhoneNumberBinding.etNewPhoneNumber.text.toString().trim()
        val password = activityChangePhoneNumberBinding.etPassword.text.toString().trim()
        if (TextUtils.isEmpty(newPhoneNumber)) {
            activityChangePhoneNumberBinding.etNewPhoneNumber.error =
                getString(R.string.edit_text_error_alert, "Nomor telepon")
            return
        }

        if (!TextUtils.isEmpty(newPhoneNumber)) {
            if (!isValidPhoneNumber(newPhoneNumber)) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.edit_text_alert_phone_number_invalid),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        if (TextUtils.isEmpty(password)) {
            activityChangePhoneNumberBinding.etPassword.error =
                getString(R.string.edit_text_error_alert, "Password")
            return
        }

        if (activityChangePhoneNumberBinding.etOldPhoneNumber.visibility == View.VISIBLE) {
            val oldPhoneNumber =
                activityChangePhoneNumberBinding.etOldPhoneNumber.text.toString().trim()
            if (oldPhoneNumber == newPhoneNumber) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.txt_success_update, "Nomor telepon"),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                return
            }
        }

        storeToDatabase(newPhoneNumber, password)
    }

    private fun storeToDatabase(newPhoneNumber: String, password: String) {
        isEnable(false)

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_loading)
        dialog.setCancelable(false)
        dialog.show()

        viewModel.updateApplicantPhoneNumber(
            applicantId,
            newPhoneNumber,
            password,
            this
        )
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length in 7..13
        } else false
    }

    private fun isEnable(state: Boolean) {
        activityChangePhoneNumberBinding.etOldPhoneNumber.isEnabled = state
        activityChangePhoneNumberBinding.etPassword.isEnabled = state
        activityChangePhoneNumberBinding.btnUpdate.isEnabled = state
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
        isEnable(true)
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_success_update, "Nomor telepon")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            finish()
        }
        dialog.show()
    }

    override fun onFailure(messageId: Int) {
        isEnable(true)
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId)
        dialog.setCancelable(false)
        dialog.show()
    }
}