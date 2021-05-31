package org.d3ifcool.dissajobapplicant.ui.media

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.media.MediaResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityAddEditMediaBinding
import org.d3ifcool.dissajobapplicant.ui.media.callback.AddMediaCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.UpdateMediaCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory

class AddEditMediaActivity : AppCompatActivity(), View.OnClickListener, UploadFileCallback,
    AddMediaCallback, UpdateMediaCallback {

    companion object {
        const val MEDIA_FILE = "media_file"
        const val MEDIA_NAME = "media_name"
        const val MEDIA_DATA = "media_data"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 102
        const val RESULT_UPDATE = 103
    }

    private lateinit var activityAddEditMediaBinding: ActivityAddEditMediaBinding

    private lateinit var viewModel: MediaViewModel

    private lateinit var dialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddEditMediaBinding = ActivityAddEditMediaBinding.inflate(layoutInflater)
        setContentView(activityAddEditMediaBinding.root)

        activityAddEditMediaBinding.toolbar.title = resources.getString(R.string.txt_media)
        setSupportActionBar(activityAddEditMediaBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[MediaViewModel::class.java]

        if (intent.extras != null) {
            val oldMediaData =
                intent.getParcelableExtra<MediaEntity>(MEDIA_DATA)
            if (oldMediaData != null) {
                activityAddEditMediaBinding.progressBar.visibility = View.VISIBLE
                viewModel.getMediaById(oldMediaData.fileId).observe(this) { file ->
                    if (file != null) {
                        activityAddEditMediaBinding.pdfViewer.fromBytes(file)
                            .enableSwipe(true)
                            .swipeHorizontal(true)
                            .load()
                    } else {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                    activityAddEditMediaBinding.progressBar.visibility = View.GONE
                }
                activityAddEditMediaBinding.etMediaName.setText(oldMediaData.mediaName)
                activityAddEditMediaBinding.etMediaDescription.setText(oldMediaData.mediaDescription)
                activityAddEditMediaBinding.btnUpload.text =
                    resources.getString(R.string.txt_update)
            } else {
                val mediaFile = Uri.parse(intent.getStringExtra(MEDIA_FILE))
                val mediaName = intent.getStringExtra(MEDIA_NAME)
                activityAddEditMediaBinding.pdfViewer.fromUri(mediaFile)
                    .enableSwipe(true)
                    .swipeHorizontal(true)
                    .load()

                activityAddEditMediaBinding.etMediaName.setText(mediaName)
            }
        }
        activityAddEditMediaBinding.btnUpload.setOnClickListener(this)
    }

    private fun uploadMedia() {
        val mediaFile = Uri.parse(intent.getStringExtra(MEDIA_FILE))
        mediaFile?.let { viewModel.uploadMedia(it, this) }
    }

    private fun storeToDatabase(fileId: String) {
        val mediaName = activityAddEditMediaBinding.etMediaName.text.toString().trim()
        var mediaDescription = activityAddEditMediaBinding.etMediaDescription.text.toString().trim()
        if (mediaDescription == "") {
            mediaDescription = "-"
        }
        val mediaData = MediaResponseEntity(
            id = "",
            mediaName = mediaName,
            mediaDescription = mediaDescription,
            applicantId = "",
            fileId = fileId
        )
        val oldMediaData = intent.getParcelableExtra<MediaEntity>(MEDIA_DATA)
        if (oldMediaData == null) {
            viewModel.addMedia(mediaData, this)
        } else {
            mediaData.id = oldMediaData.id
            mediaData.applicantId = oldMediaData.applicantId
            viewModel.updateMedia(mediaData, this)
        }
    }

    private fun formValidation() {
        val mediaName = activityAddEditMediaBinding.etMediaName.text.toString().trim()

        if (TextUtils.isEmpty(mediaName)) {
            activityAddEditMediaBinding.etMediaName.error =
                getString(R.string.edit_text_error_alert, "Judul")
            return
        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_loading)
        dialog.setCancelable(false)
        dialog.show()

        val oldMediaData = intent.getParcelableExtra<MediaEntity>(MEDIA_DATA)
        if (oldMediaData == null) {
            uploadMedia()
        } else {
            storeToDatabase(oldMediaData.fileId)
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
            R.id.btnUpload -> formValidation()
        }
    }

    override fun onSuccessUpload(fileId: String) {
        storeToDatabase(fileId)
    }

    override fun onFailureUpload(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId, "Media")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }

    override fun onSuccessAdding() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.tv_success_adding_media)
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
        dialog.titleText = resources.getString(messageId, "Media")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }

    override fun onSuccessUpdate() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_success_update, "Media")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            setResult(RESULT_UPDATE)
            finish()
        }
        dialog.show()
    }

    override fun onFailureUpdate(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(messageId, "Media")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }
}