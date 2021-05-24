package org.d3ifcool.dissajobapplicant.ui.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.applicant.ApplicantResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityChangeProfileBinding
import org.d3ifcool.dissajobapplicant.ui.profile.ApplicantViewModel
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UpdateProfileCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.database.AuthHelper
import org.d3ifcool.dissajobapplicant.vo.Status

class ChangeProfileActivity : AppCompatActivity(), View.OnClickListener, UpdateProfileCallback,
    UploadFileCallback {

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000

        //Permission code
        private const val PERMISSION_CODE = 1001
    }

    private lateinit var activityChangeProfileBinding: ActivityChangeProfileBinding

    private lateinit var viewModel: ApplicantViewModel

    private lateinit var image: Uri

    private lateinit var dialog: SweetAlertDialog

    private var isUpdateImage = false

    private lateinit var applicantData: ApplicantEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangeProfileBinding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(activityChangeProfileBinding.root)

        activityChangeProfileBinding.toolbar.title =
            resources.getString(R.string.txt_change_profile_title)
        setSupportActionBar(activityChangeProfileBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]

        showCurrentProfileData()

        activityChangeProfileBinding.imgProfilePic.setOnClickListener(this)
        activityChangeProfileBinding.btnUpdate.setOnClickListener(this)
    }

    private fun showCurrentProfileData() {
        viewModel.getApplicantDetails(AuthHelper.currentUser?.uid.toString())
            .observe(this) { profileData ->
                if (profileData.data != null) {
                    when (profileData.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            applicantData = profileData.data
                            if (profileData.data.imagePath != "-") {
                                val storageRef = Firebase.storage.reference
                                val circularProgressDrawable = CircularProgressDrawable(this)
                                circularProgressDrawable.strokeWidth = 5f
                                circularProgressDrawable.centerRadius = 30f
                                circularProgressDrawable.start()

                                Glide.with(this)
                                    .load(storageRef.child("applicant/profile/images/${profileData.data.imagePath}"))
                                    .placeholder(circularProgressDrawable)
                                    .apply(RequestOptions.overrideOf(500, 500)).centerCrop()
                                    .into(activityChangeProfileBinding.imgProfilePic)
                            }

                            activityChangeProfileBinding.etFirstName.setText(profileData.data.firstName)
                            activityChangeProfileBinding.etLastName.setText(profileData.data.lastName)
                            activityChangeProfileBinding.etAboutMe.setText(profileData.data.aboutMe)
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                //permission already granted
                pickImageFromGallery()
            }
        } else {
            //system OS is < Marshmallow
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data?.data != null) {
                image = data.data!!
                activityChangeProfileBinding.imgProfilePic.setImageURI(data.data)
                isUpdateImage = true
            }
        }
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(activityChangeProfileBinding.etFirstName.text.toString().trim())) {
            activityChangeProfileBinding.etFirstName.error =
                getString(R.string.edit_text_error_alert, "Nama depan")
            return
        }
        if (TextUtils.isEmpty(activityChangeProfileBinding.etLastName.text.toString().trim())) {
            activityChangeProfileBinding.etLastName.error =
                getString(R.string.edit_text_error_alert, "Nama belakang")
            return
        }
        if (TextUtils.isEmpty(activityChangeProfileBinding.etAboutMe.text.toString().trim())) {
            activityChangeProfileBinding.etAboutMe.error =
                getString(R.string.edit_text_error_alert, "Tentang saya")
            return
        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_loading)
        dialog.setCancelable(false)
        dialog.show()

        if (isUpdateImage) {
            viewModel.uploadApplicantProfilePicture(image, this)
        } else {
            updateUserProfile(applicantData.imagePath.toString())
        }
    }

    private fun updateUserProfile(imageId: String) {
        val firstName = activityChangeProfileBinding.etFirstName.text.toString().trim()
        val lastName = activityChangeProfileBinding.etLastName.text.toString().trim()
        val fullName = "$firstName $lastName"
        val email = applicantData.email.toString()
        val aboutMe = activityChangeProfileBinding.etAboutMe.text.toString().trim()
        val phoneNumber = applicantData.phoneNumber.toString()
        val profileData = ApplicantResponseEntity(
            AuthHelper.currentUser?.uid.toString(),
            firstName,
            lastName,
            fullName,
            email,
            aboutMe,
            phoneNumber,
            imageId
        )

        viewModel.updateApplicantData(profileData, this)
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
            R.id.imgProfilePic -> checkPermission()
            R.id.btnUpdate -> {
                formValidation()
            }
        }
    }

    override fun onSuccess() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_success_update, "Profil")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            finish()
        }
        dialog.show()
    }

    override fun onFailure(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onSuccessUpload(fileId: String) {
        updateUserProfile(fileId)
    }

    override fun onFailureUpload(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId)
        dialog.setCancelable(false)
        dialog.show()
    }
}