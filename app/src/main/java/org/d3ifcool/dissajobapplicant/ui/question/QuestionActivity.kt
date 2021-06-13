package org.d3ifcool.dissajobapplicant.ui.question

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.application.ApplicationResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.interview.InterviewResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityQuestionBinding
import org.d3ifcool.dissajobapplicant.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobapplicant.ui.job.callback.ApplyJobCallback
import org.d3ifcool.dissajobapplicant.ui.profile.ApplicantViewModel
import org.d3ifcool.dissajobapplicant.ui.profile.callback.CheckApplicantDataCallback
import org.d3ifcool.dissajobapplicant.ui.settings.ChangePhoneNumberActivity
import org.d3ifcool.dissajobapplicant.ui.settings.ChangeProfileActivity
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.DateUtils
import org.d3ifcool.dissajobapplicant.utils.database.AuthHelper

class QuestionActivity : AppCompatActivity(), InsertInterviewAnswersCallback, View.OnClickListener,
    ApplyJobCallback, CheckApplicantDataCallback {

    companion object {
        const val JOB_ID = "job_id"
        const val APPLICATION_ID = "application_id"
        const val REQUEST_APPLY = 100
        const val RESULT_APPLY = 101
    }

    private lateinit var activityQuestionBinding: ActivityQuestionBinding

    private lateinit var interviewViewModel: InterviewViewModel

    private lateinit var applicationViewModel: ApplicationViewModel

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var dialog: SweetAlertDialog

    private lateinit var jobId: String

    private lateinit var applicationId: String

    private var isBtnClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityQuestionBinding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(activityQuestionBinding.root)

        activityQuestionBinding.toolbar.title =
            resources.getString(R.string.txt_additional_information)
        setSupportActionBar(activityQuestionBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent.extras == null) {
            finish()
            return
        }

        jobId = intent.getStringExtra(JOB_ID).toString()

        val factory = ViewModelFactory.getInstance(this)
        interviewViewModel = ViewModelProvider(this, factory)[InterviewViewModel::class.java]
        applicationViewModel = ViewModelProvider(this, factory)[ApplicationViewModel::class.java]
        applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]

        activityQuestionBinding.btnSubmit.setOnClickListener(this)
    }

    private fun storeToDatabase() {
        val application = ApplicationResponseEntity(
            "",
            "",
            jobId,
            DateUtils.getCurrentDate(),
            "-",
            "Waiting",
            false
        )
        applicationViewModel.insertApplication(application, this)
    }

    private fun formValidation() {
        val firstAnswer = activityQuestionBinding.etFirstQuestion.text.toString().trim()
        val secondAnswer = activityQuestionBinding.etSecondQuestion.text.toString().trim()
        val thirdAnswer = activityQuestionBinding.etThirdQuestion.text.toString().trim()

        if (TextUtils.isEmpty(firstAnswer)) {
            activityQuestionBinding.etFirstQuestion.error =
                getString(R.string.edit_text_error_alert, "Kolom")
            return
        }

        if (TextUtils.isEmpty(secondAnswer)) {
            activityQuestionBinding.etSecondQuestion.error =
                getString(R.string.edit_text_error_alert, "Kolom")
            return
        }

        if (TextUtils.isEmpty(thirdAnswer)) {
            activityQuestionBinding.etThirdQuestion.error =
                getString(R.string.edit_text_error_alert, "Kolom")
            return
        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(R.string.txt_alert_apply)
        dialog.confirmText = resources.getString(R.string.txt_submit)
        dialog.cancelText = resources.getString(R.string.txt_cancel)
        dialog.setCancelable(false)
        dialog.showCancelButton(true)
        dialog.setConfirmClickListener {
            dialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
            dialog.titleText = resources.getString(R.string.txt_loading)
            dialog.setCancelable(false)
            dialog.showCancelButton(false)
            storeToDatabase()
        }.setCancelClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }

    private fun showToast() = Toast.makeText(
        this,
        resources.getString(R.string.txt_fill_all_data_alert),
        Toast.LENGTH_SHORT
    ).show()

    override fun onSuccessApply(applicationId: String) {
        this.applicationId = applicationId

        val firstAnswer = activityQuestionBinding.etFirstQuestion.text.toString().trim()
        val secondAnswer = activityQuestionBinding.etSecondQuestion.text.toString().trim()
        val thirdAnswer = activityQuestionBinding.etThirdQuestion.text.toString().trim()

        val interviewAnswer = InterviewResponseEntity(
            "",
            applicationId,
            "",
            firstAnswer,
            secondAnswer,
            thirdAnswer
        )
        interviewViewModel.insertInterviewAnswers(interviewAnswer, this)
    }

    override fun onFailureApply(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId)
        dialog.confirmText = resources.getString(R.string.dialog_ok)
        dialog.setCancelable(false)
        dialog.showCancelButton(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }

    override fun onSuccessAdding() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_success_apply)
        dialog.confirmText = resources.getString(R.string.dialog_ok)
        dialog.setCancelable(false)
        dialog.showCancelButton(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            val intent = Intent()
            intent.putExtra(APPLICATION_ID, applicationId)
            setResult(RESULT_APPLY, intent)
            finish()
        }
        dialog.show()
    }

    override fun onFailureAdding(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId)
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
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
            R.id.btnSubmit -> {
                isBtnClicked = true
                applicantViewModel.checkApplicantData(AuthHelper.currentUser?.uid.toString(), this)
            }
        }
    }

    override fun allDataAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            formValidation()
        }
    }

    override fun profileDataNotAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            showToast()
            startActivity(Intent(this, ChangeProfileActivity::class.java))
        }
    }

    override fun phoneNumberNotAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            showToast()
            startActivity(Intent(this, ChangePhoneNumberActivity::class.java))
        }
    }
}