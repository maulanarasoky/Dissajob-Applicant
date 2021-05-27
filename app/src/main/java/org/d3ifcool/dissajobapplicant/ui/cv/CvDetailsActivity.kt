package org.d3ifcool.dissajobapplicant.ui.cv

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityCvDetailsBinding
import org.d3ifcool.dissajobapplicant.ui.cv.callback.AddCvCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory

class CvDetailsActivity : AppCompatActivity(), View.OnClickListener, UploadFileCallback,
    AddCvCallback {

    companion object {
        const val CV_FILE = "cv_file"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
    }

    private lateinit var activityCvDetailsBinding: ActivityCvDetailsBinding

    private lateinit var viewModel: CvViewModel

    private lateinit var dialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCvDetailsBinding = ActivityCvDetailsBinding.inflate(layoutInflater)
        setContentView(activityCvDetailsBinding.root)

        activityCvDetailsBinding.toolbar.title = resources.getString(R.string.txt_curriculum_vitae)
        setSupportActionBar(activityCvDetailsBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[CvViewModel::class.java]

        val cvFile = Uri.parse(intent.getStringExtra(CV_FILE))
        activityCvDetailsBinding.pdfViewer.fromUri(cvFile)
            .enableSwipe(true)
            .swipeHorizontal(true)
            .load()

        activityCvDetailsBinding.btnUpload.setOnClickListener(this)
    }

    private fun uploadCv() {
        val cvFile = Uri.parse(intent.getStringExtra(CV_FILE))
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
        viewModel.addCv(cvData, this)
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

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_loading)
        dialog.setCancelable(false)
        dialog.show()

        uploadCv()
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
            R.id.btnUpload -> formValidation()
        }
    }

    override fun onSuccessUpload(fileId: String) {
        storeToDatabase(fileId)
    }

    override fun onFailureUpload(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId, "Cv")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }

    override fun onSuccessAdding() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.tv_success_adding_cv)
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            setResult(RESULT_ADD)
            finish()
        }
        dialog.show()
    }

    override fun onFailureAdding(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId, "Cv")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }
}