package org.d3ifcool.dissajobapplicant.ui.job

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.SavedJobResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityJobDetailsBinding
import org.d3ifcool.dissajobapplicant.ui.application.ApplicationDetailsActivity
import org.d3ifcool.dissajobapplicant.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobapplicant.ui.job.callback.SaveJobCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.UnSaveJobCallback
import org.d3ifcool.dissajobapplicant.ui.profile.ApplicantViewModel
import org.d3ifcool.dissajobapplicant.ui.profile.callback.CheckApplicantDataCallback
import org.d3ifcool.dissajobapplicant.ui.question.QuestionActivity
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterProfileActivity
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.settings.ChangePhoneNumberActivity
import org.d3ifcool.dissajobapplicant.ui.settings.ChangeProfileActivity
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.DateUtils
import org.d3ifcool.dissajobapplicant.vo.Status

class JobDetailsActivity : AppCompatActivity(), View.OnClickListener, SaveJobCallback,
    UnSaveJobCallback, CheckApplicantDataCallback {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var activityJobDetailsBinding: ActivityJobDetailsBinding

    private lateinit var jobViewModel: JobViewModel

    private lateinit var applicationViewModel: ApplicationViewModel

    private lateinit var recruiterViewModel: RecruiterViewModel

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var jobData: JobDetailsEntity

    private var isJobApplied = false
    private var isJobSaved = false

    private lateinit var jobId: String
    private lateinit var applicationId: String
    private lateinit var saveJobId: String

    private var isBtnClicked = false

    private val applicantId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityJobDetailsBinding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(activityJobDetailsBinding.root)

        activityJobDetailsBinding.toolbar.title =
            resources.getString(R.string.txt_job_details_toolbar_title)
        setSupportActionBar(activityJobDetailsBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent != null) {
            val factory = ViewModelFactory.getInstance(this)
            recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
            applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
            jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
            applicationViewModel =
                ViewModelProvider(this, factory)[ApplicationViewModel::class.java]
            jobId = intent.getStringExtra(EXTRA_ID).toString()
            showJobDetails(jobId)
            isJobApplied()
            isJobSaved()
        }

        activityJobDetailsBinding.jobDetailsFooterSection.btnApply.setOnClickListener(this)
        activityJobDetailsBinding.jobDetailsFooterSection.btnSave.setOnClickListener(this)
    }

    private fun isJobApplied() {
        applicationViewModel.getApplicationByJob(jobId, applicantId)
            .observe(this) { application ->
                when (application.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        if (application != null) {
                            activityJobDetailsBinding.jobDetailsFooterSection.btnApply.text =
                                resources.getString(R.string.txt_my_application)
                            applicationId = application.data?.id.toString()
                            isJobApplied = true
                        } else {
                            activityJobDetailsBinding.jobDetailsFooterSection.btnApply.text =
                                resources.getString(R.string.txt_apply)
                            isJobApplied = false
                        }
                    }
                    Status.ERROR -> showToast(resources.getString(R.string.txt_error_occurred))
                }
            }
    }

    private fun isJobSaved() {
        jobViewModel.getSavedJobByJob(jobId, applicantId)
            .observe(this) { saveJob ->
                when (saveJob.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        if (saveJob != null) {
                            activityJobDetailsBinding.jobDetailsFooterSection.btnSave.text =
                                resources.getString(R.string.txt_saved)
                            saveJobId = saveJob.data?.id.toString()
                            isJobSaved = true
                        } else {
                            activityJobDetailsBinding.jobDetailsFooterSection.btnSave.text =
                                resources.getString(R.string.txt_save)
                            isJobSaved = false
                        }
                    }
                    Status.ERROR -> showToast(resources.getString(R.string.txt_error_occurred))
                }
            }
    }

    private fun showJobDetails(jobId: String) {
        jobViewModel.getJobDetails(jobId).observe(this) { jobDetails ->
            if (jobDetails.data != null) {
                when (jobDetails.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        jobData = jobDetails.data
                        populateJobData(jobDetails.data)
                    }
                    Status.ERROR -> showToast(resources.getString(R.string.txt_error_occurred))
                }
            }
        }
    }

    private fun populateJobData(jobDetails: JobDetailsEntity) {
        //Title section
        activityJobDetailsBinding.jobDetailsTitleSection.tvJobTitle.text =
            jobDetails.title.toString()

        activityJobDetailsBinding.jobDetailsTitleSection.tvJobAddress.text =
            jobDetails.address.toString()

        populateRecruiterData(jobDetails.postedBy)

        val postedDate = DateUtils.getPostedDate(jobDetails.postedDate.toString())
        activityJobDetailsBinding.jobDetailsTitleSection.tvJobType.text = resources.getString(
            R.string.txt_job_type,
            jobDetails.employment.toString(),
            jobDetails.type.toString()
        )
        activityJobDetailsBinding.jobDetailsTitleSection.tvJobPostedDate.text = postedDate

        if (jobDetails.isOpenForDisability) {
            activityJobDetailsBinding.jobDetailsTitleSection.tvOpenForDisability.visibility =
                View.VISIBLE
        } else {
            activityJobDetailsBinding.jobDetailsTitleSection.tvOpenForDisability.visibility =
                View.GONE
        }

        if (!jobDetails.isOpen) {
            activityJobDetailsBinding.jobDetailsTitleSection.tvCloseRecruitment.visibility =
                View.VISIBLE
        } else {
            activityJobDetailsBinding.jobDetailsTitleSection.tvCloseRecruitment.visibility =
                View.GONE
        }

        //Description section
        activityJobDetailsBinding.jobDetailsDescriptionSection.tvJobDescription.text =
            jobDetails.description.toString()

        if (jobDetails.isOpenForDisability) {
            if (jobDetails.additionalInformation.toString() != "-") {
                activityJobDetailsBinding.jobDetailsDescriptionSection.tvAdditionalInformation.visibility =
                    View.VISIBLE
                activityJobDetailsBinding.jobDetailsDescriptionSection.tvAdditionalInformation.text =
                    resources.getString(
                        R.string.job_details_additional_information,
                        jobData.additionalInformation
                    )
            }
        }

        //Details section
        activityJobDetailsBinding.jobDetailsDetailsSection.tvJobQualification.text =
            jobDetails.qualification.toString()
        activityJobDetailsBinding.jobDetailsDetailsSection.tvJobIndustry.text =
            jobDetails.industry.toString()

        activityJobDetailsBinding.jobDetailsDetailsSection.tvJobSalary.text =
            jobDetails.salary.toString()
    }

    private fun populateRecruiterData(recruiterId: String) {
        recruiterViewModel.getRecruiterData(recruiterId).observe(this) { recruiterDetails ->
            if (recruiterDetails != null) {
                val recruiterData = recruiterDetails.data
                if (recruiterData != null) {
                    activityJobDetailsBinding.jobDetailsTitleSection.tvJobRecruiterName.text =
                        recruiterData.fullName.toString()

                    if (recruiterData.imagePath != "-") {
                        val storageRef = Firebase.storage.reference
                        val circularProgressDrawable = CircularProgressDrawable(this)
                        circularProgressDrawable.strokeWidth = 5f
                        circularProgressDrawable.centerRadius = 30f
                        circularProgressDrawable.start()
                        Glide.with(this)
                            .load(storageRef.child("recruiter/profile/images/${recruiterData.imagePath}"))
                            .transform(RoundedCorners(20))
                            .apply(RequestOptions.placeholderOf(circularProgressDrawable))
                            .error(R.drawable.ic_image_gray_24dp)
                            .into(activityJobDetailsBinding.jobDetailsTitleSection.imgRecruiterPicture)
                    }

                    activityJobDetailsBinding.jobDetailsTitleSection.tvJobRecruiterName.setOnClickListener(
                        this
                    )
                }
            }
        }
    }

    private fun checkApplyCondition() {
        if (!jobData.isOpen) {
            if (!isJobApplied) {
                showToast(resources.getString(R.string.txt_close_recruitment))
                return
            }
        }

        if (isJobApplied) {
            val intent = Intent(this, ApplicationDetailsActivity::class.java)
            intent.putExtra(ApplicationDetailsActivity.APPLICATION_ID, applicationId)
            intent.putExtra(ApplicationDetailsActivity.JOB_ID, jobData.id)
            intent.putExtra(ApplicationDetailsActivity.RECRUITER_ID, jobData.postedBy)
            startActivity(intent)
        } else {
            isBtnClicked = true
            applicantViewModel.checkApplicantData(applicantId, this)
        }
    }

    private fun checkSaveCondition() {
        activityJobDetailsBinding.jobDetailsFooterSection.btnSave.isEnabled = false
        if (isJobSaved) {
            unSaveJob()
        } else {
            saveJob()
        }
    }

    private fun saveJob() {
        val jobData = SavedJobResponseEntity(
            "",
            jobData.id,
            applicantId
        )
        jobViewModel.saveJob(jobData, this)
    }

    private fun unSaveJob() {
        jobViewModel.unSaveJob(saveJobId, this)
    }

    private fun showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

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
            R.id.tvJobRecruiterName -> {
                val intent = Intent(this, RecruiterProfileActivity::class.java)
                intent.putExtra(RecruiterProfileActivity.RECRUITER_ID, jobData.postedBy)
                startActivity(intent)
            }
            R.id.btnApply -> checkApplyCondition()
            R.id.btnSave -> checkSaveCondition()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == QuestionActivity.REQUEST_APPLY) {
            if (resultCode == QuestionActivity.RESULT_APPLY) {
                activityJobDetailsBinding.jobDetailsFooterSection.btnApply.text =
                    resources.getString(R.string.txt_my_application)
                applicationId = data?.getStringExtra(QuestionActivity.APPLICATION_ID).toString()
                isJobApplied = true
            }
        }
    }

    override fun onSuccessSave(saveJobId: String) {
        this.saveJobId = saveJobId
        activityJobDetailsBinding.jobDetailsFooterSection.btnSave.text =
            resources.getString(R.string.txt_saved)
        activityJobDetailsBinding.jobDetailsFooterSection.btnSave.isEnabled = true
        Toast.makeText(this, resources.getString(R.string.txt_success_save_job), Toast.LENGTH_SHORT)
            .show()
        isJobSaved = true
    }

    override fun onFailureSave(messageId: Int) {
        activityJobDetailsBinding.jobDetailsFooterSection.btnSave.isEnabled = true
        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
        isJobSaved = false
    }

    override fun onSuccessUnSave() {
        activityJobDetailsBinding.jobDetailsFooterSection.btnSave.text =
            resources.getString(R.string.txt_save)
        activityJobDetailsBinding.jobDetailsFooterSection.btnSave.isEnabled = true
        isJobSaved = false
    }

    override fun onFailureUnSave(messageId: Int) {
        activityJobDetailsBinding.jobDetailsFooterSection.btnSave.isEnabled = true
        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
        isJobSaved = true
    }

    override fun allDataAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            val intent = Intent(this, QuestionActivity::class.java)
            intent.putExtra(QuestionActivity.JOB_ID, jobData.id)
            intent.putExtra(QuestionActivity.RECRUITER_ID, jobData.postedBy)
            startActivityForResult(intent, QuestionActivity.REQUEST_APPLY)
        }
    }

    override fun profileDataNotAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            showToast(resources.getString(R.string.txt_fill_all_data_alert))
            startActivity(Intent(this, ChangeProfileActivity::class.java))
        }
    }

    override fun phoneNumberNotAvailable() {
        if (isBtnClicked) {
            isBtnClicked = false
            showToast(resources.getString(R.string.txt_fill_all_data_alert))
            startActivity(Intent(this, ChangePhoneNumberActivity::class.java))
        }
    }
}