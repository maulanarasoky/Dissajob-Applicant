package org.d3ifcool.dissajobapplicant.ui.cv

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityCvDetailsBinding
import org.d3ifcool.dissajobapplicant.ui.profile.ApplicantViewModel
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.AuthHelper
import org.d3ifcool.dissajobapplicant.utils.InsertToDatabaseCallback

class CvDetailsActivity : AppCompatActivity(), View.OnClickListener, UploadFileCallback, InsertToDatabaseCallback {

    companion object {
        const val CV_FILE = "cv_file"
    }

    private lateinit var activityCvDetailsBinding: ActivityCvDetailsBinding

    private lateinit var viewModel: CvViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCvDetailsBinding = ActivityCvDetailsBinding.inflate(layoutInflater)
        setContentView(activityCvDetailsBinding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[CvViewModel::class.java]

        if (intent.extras != null) {
            val cvFile = intent.getParcelableExtra<Uri>(CV_FILE)
            activityCvDetailsBinding.pdfViewer.fromUri(cvFile)
        }

        activityCvDetailsBinding.btnUpload.setOnClickListener(this)
    }

    private fun uploadCv() {
        val cvFile = intent.getParcelableExtra<Uri>(CV_FILE)
        cvFile?.let { viewModel.uploadCv(it, this) }
    }

    private fun storeToDatabase(fileId: String) {
        val cvName = activityCvDetailsBinding.etCvName.text.toString().trim()
        val cvDescription = activityCvDetailsBinding.etCvDescription.text.toString().trim()
        val cvData = CvResponseEntity(
            id = "",
            cvName = cvName,
            cvDescription = cvDescription,
            applicantId = "",
            fileId = fileId
        )
        viewModel.storeFileId(cvData, this)
    }

    private fun formValidation() {
        val cvName = activityCvDetailsBinding.etCvName.text.toString().trim()
        val cvDescription = activityCvDetailsBinding.etCvDescription.text.toString().trim()

        if (TextUtils.isEmpty(cvName)) {
            activityCvDetailsBinding.etCvName.error =
                getString(R.string.edit_text_error_alert, "Judul")
            return
        }

        if (TextUtils.isEmpty(cvDescription)) {
            activityCvDetailsBinding.etCvDescription.error =
                getString(R.string.edit_text_error_alert, "Deskripsi")
            return
        }

        uploadCv()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnUpload -> formValidation()
        }
    }

    override fun onSuccessUpload(fileId: String) {
        storeToDatabase(fileId)
    }

    override fun onFailureUpload(messageId: Int) {
    }

    override fun onSuccess() {
    }

    override fun onFailure(messageId: Int) {
    }
}