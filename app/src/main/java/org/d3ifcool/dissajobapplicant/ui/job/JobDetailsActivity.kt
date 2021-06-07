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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.job.SavedJobResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityJobDetailsBinding
import org.d3ifcool.dissajobapplicant.ui.job.callback.SaveJobCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.UnSaveJobCallback
import org.d3ifcool.dissajobapplicant.ui.question.QuestionActivity
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterProfileActivity
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.DateUtils
import org.d3ifcool.dissajobapplicant.vo.Status

class JobDetailsActivity : AppCompatActivity(), View.OnClickListener, SaveJobCallback,
    UnSaveJobCallback {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var activityJobDetailsBinding: ActivityJobDetailsBinding

    private lateinit var jobViewModel: JobViewModel

    private lateinit var recruiterViewModel: RecruiterViewModel

    private lateinit var jobData: JobDetailsEntity

    private var isJobApplied = false
    private var isJobSaved = false

    lateinit var saveJobId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityJobDetailsBinding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(activityJobDetailsBinding.root)

        activityJobDetailsBinding.toolbar.title =
            resources.getString(R.string.txt_job_details_toolbar_title)
        setSupportActionBar(activityJobDetailsBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            val factory = ViewModelFactory.getInstance(this)
            recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
            jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
            val jobId = extras.getString(EXTRA_ID)
            if (jobId != null) {
                showJobDetails(jobId)
            }
        }

        activityJobDetailsBinding.jobDetailsFooterSection.btnApply.setOnClickListener(this)
        activityJobDetailsBinding.jobDetailsFooterSection.btnSave.setOnClickListener(this)
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
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
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
        }

        //Description section
        activityJobDetailsBinding.jobDetailsDescriptionSection.tvJobDescription.text =
            jobDetails.description.toString()

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
        if (isJobApplied) {
            val intent = Intent(this, ApplicationEntity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, QuestionActivity::class.java)
            intent.putExtra(QuestionActivity.JOB_ID, jobData.id)
            startActivityForResult(intent, QuestionActivity.REQUEST_ADD)
        }
    }

    private fun checkSaveCondition() {
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
            ""
        )
        jobViewModel.saveJob(jobData, this)
    }

    private fun unSaveJob() {
        jobViewModel.unSaveJob(saveJobId, this)
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
        if (requestCode == QuestionActivity.REQUEST_ADD) {
            if (resultCode == QuestionActivity.RESULT_ADD) {
                activityJobDetailsBinding.jobDetailsFooterSection.btnApply.isEnabled = false
                isJobApplied = true
            }
        }
    }

    override fun onSuccessSave(saveJobId: String) {
        this.saveJobId = saveJobId
        activityJobDetailsBinding.jobDetailsFooterSection.btnSave.text =
            resources.getString(R.string.txt_saved)
        Toast.makeText(this, resources.getString(R.string.txt_success_save_job), Toast.LENGTH_SHORT)
            .show()
        isJobSaved = true
    }

    override fun onFailureSave(messageId: Int) {
        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
        isJobSaved = false
    }

    override fun onSuccessUnSave() {
        activityJobDetailsBinding.jobDetailsFooterSection.btnSave.text =
            resources.getString(R.string.txt_save)

        isJobSaved = false
    }

    override fun onFailureUnSave(messageId: Int) {
        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
        isJobSaved = true
    }
}