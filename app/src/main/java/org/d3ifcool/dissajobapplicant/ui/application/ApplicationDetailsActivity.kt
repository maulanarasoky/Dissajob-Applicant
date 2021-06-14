package org.d3ifcool.dissajobapplicant.ui.application

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityApplicationDetailsBinding
import org.d3ifcool.dissajobapplicant.ui.job.JobDetailsActivity
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.question.InterviewViewModel
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterProfileActivity
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.vo.Status

class ApplicationDetailsActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val APPLICATION_ID = "application_id"
        const val JOB_ID = "job_id"
        const val RECRUITER_ID = "recruiter_id"
    }

    private lateinit var activityApplicationDetailsBinding: ActivityApplicationDetailsBinding

    private lateinit var recruiterViewModel: RecruiterViewModel

    private lateinit var applicationViewModel: ApplicationViewModel

    private lateinit var jobViewModel: JobViewModel

    private lateinit var interviewViewModel: InterviewViewModel

    private lateinit var recruiterId: String
    private lateinit var jobId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApplicationDetailsBinding =
            ActivityApplicationDetailsBinding.inflate(layoutInflater)
        setContentView(activityApplicationDetailsBinding.root)

        activityApplicationDetailsBinding.toolbar.title =
            resources.getString(R.string.txt_my_application)
        setSupportActionBar(activityApplicationDetailsBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val applicationId = intent.getStringExtra(APPLICATION_ID)
        recruiterId = intent.getStringExtra(RECRUITER_ID).toString()
        jobId = intent.getStringExtra(JOB_ID).toString()

        val factory = ViewModelFactory.getInstance(this)
        recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
        applicationViewModel = ViewModelProvider(this, factory)[ApplicationViewModel::class.java]
        jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
        interviewViewModel = ViewModelProvider(this, factory)[InterviewViewModel::class.java]

        recruiterViewModel.getRecruiterData(recruiterId).observe(this) { profile ->
            if (profile.data != null) {
                when (profile.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> populateRecruiterData(profile.data)
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        applicationViewModel.getApplicationById(applicationId.toString())
            .observe(this) { application ->
                if (application.data != null) {
                    when (application.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            val applicationData = ApplicationEntity(
                                application.data.id,
                                application.data.applicantId,
                                application.data.jobId,
                                application.data.applyDate.toString(),
                                application.data.updatedDate.toString(),
                                application.data.status.toString(),
                                application.data.isMarked.toString().toBoolean(),
                                application.data.recruiterId
                            )
                            populateApplicationData(applicationData)
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        jobViewModel.getJobDetails(jobId).observe(this) { jobDetails ->
            if (jobDetails.data != null) {
                when (jobDetails.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        populateJobData(
                            jobDetails.data.title.toString(),
                            jobDetails.data.description.toString()
                        )
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        interviewViewModel.getInterviewAnswers(applicationId.toString())
            .observe(this) { interview ->
                if (interview.data != null) {
                    when (interview.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> populateInterviewData(
                            interview.data.firstAnswer.toString(),
                            interview.data.secondAnswer.toString(),
                            interview.data.thirdAnswer.toString()
                        )
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        activityApplicationDetailsBinding.recruiterProfileSection.root.setOnClickListener(this)
        activityApplicationDetailsBinding.applicationDetailsSection.root.setOnClickListener(this)
    }

    private fun populateRecruiterData(recruiterProfile: RecruiterEntity) {
        activityApplicationDetailsBinding.recruiterProfileSection.tvRecruiterName.text =
            recruiterProfile.fullName.toString()
        activityApplicationDetailsBinding.recruiterProfileSection.tvEmail.text =
            recruiterProfile.email.toString()
        activityApplicationDetailsBinding.recruiterProfileSection.tvPhoneNumber.text =
            recruiterProfile.phoneNumber.toString()
        activityApplicationDetailsBinding.recruiterProfileSection.tvRecruiterAddress.text =
            recruiterProfile.address.toString()

        if (recruiterProfile.imagePath != "-") {
            val storageRef = Firebase.storage.reference
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(this)
                .load(storageRef.child("recruiter/profile/images/${recruiterProfile.imagePath}"))
                .transform(RoundedCorners(20))
                .apply(RequestOptions.placeholderOf(circularProgressDrawable))
                .error(R.drawable.ic_profile_gray_24dp)
                .into(activityApplicationDetailsBinding.recruiterProfileSection.imgRecruiterPicture)
        }
    }

    private fun populateApplicationData(applicationData: ApplicationEntity) {

        activityApplicationDetailsBinding.applicationDetailsSection.tvApplyDate.text =
            applicationData.applyDate

        when (applicationData.status) {
            "Waiting" -> {
                activityApplicationDetailsBinding.applicationDetailsSection.tvApplicationStatus.setTextColor(
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange))
                )
            }
            "Accepted" -> {
                activityApplicationDetailsBinding.applicationDetailsSection.tvApplicationStatus.setTextColor(
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green))
                )
            }
            "Rejected" -> {
                activityApplicationDetailsBinding.applicationDetailsSection.tvApplicationStatus.setTextColor(
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
                )
            }
        }
        activityApplicationDetailsBinding.applicationDetailsSection.tvApplicationStatus.text =
            applicationData.status.toString()
    }

    private fun populateJobData(jobTitle: String, jobDescription: String) {
        activityApplicationDetailsBinding.applicationDetailsSection.tvJobTitle.text = jobTitle
        activityApplicationDetailsBinding.applicationDetailsSection.tvJobDescription.text =
            jobDescription
    }

    private fun populateInterviewData(
        firstAnswer: String,
        secondAnswer: String,
        thirdAnswer: String
    ) {
        activityApplicationDetailsBinding.additionalInformationSection.etFirstQuestion.setText(
            firstAnswer
        )
        activityApplicationDetailsBinding.additionalInformationSection.etSecondQuestion.setText(
            secondAnswer
        )
        activityApplicationDetailsBinding.additionalInformationSection.etThirdQuestion.setText(
            thirdAnswer
        )
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
            R.id.recruiterProfileSection -> {
                val intent = Intent(this, RecruiterProfileActivity::class.java)
                intent.putExtra(RecruiterProfileActivity.RECRUITER_ID, recruiterId)
                startActivity(intent)
            }
            R.id.applicationDetailsSection -> {
                val intent = Intent(this, JobDetailsActivity::class.java)
                intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
                startActivity(intent)
            }
        }
    }
}